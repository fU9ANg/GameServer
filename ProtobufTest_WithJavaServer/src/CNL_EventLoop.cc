/*
 * CNL_EventLoop.cc
 *
 *  Created on: 2016年4月8日
 *      Author: newman
 */

#include "CNL_Defines.h"
#include "PROTOCOL.pb.h"
#include "CNL_EventLoop.h"
#include "CNL_MessageQueue.h"

struct ev_loop* EventLoop::loop = NULL;
//struct ev_io_info EventLoop::ioarray[MAXFD];

EventLoop::EventLoop(string ip, int port) {
    m_ip = ip;
    m_port = port;
    m_connectfd = socket(AF_INET, SOCK_STREAM, 0);
//    setnonblock(m_connectfd);
//    setreuseaddr (m_connectfd);
//    setnodelay (m_connectfd);
    std::cout << "m_connectfd: " << m_connectfd << std::endl;
}

EventLoop::~EventLoop() {
    close(m_connectfd);
}

int EventLoop::work() {
//    startlisten();
    //
    std::cout << "work()" << "ip:" << m_ip << ", port:" << m_port << std::endl;
    struct sockaddr_in cliaddr;
    bzero(&cliaddr, sizeof(struct sockaddr_in));
    cliaddr.sin_family = AF_INET;
    cliaddr.sin_addr.s_addr = inet_addr(m_ip.c_str());
    cliaddr.sin_port = htons(m_port);
    int rlt = connect(m_connectfd, (struct sockaddr*) &cliaddr, sizeof(struct sockaddr));
    cout << errno << std::endl;
    perror("connecT:");

    std::cout << "rlt:" << rlt << std::endl;
    if(rlt == -1 && errno != EINPROGRESS) {
        printf("connect failllllllllll !\n");
        return -1;
    }
    ev_io *ev_io_watcher = (ev_io*) malloc(sizeof(ev_io));
    EventLoop::loop = ev_loop_new(EVBACKEND_EPOLL);

    ev_io_init(ev_io_watcher, &recv_cb, m_connectfd, EV_READ | EV_WRITE);

    ev_io_start(EventLoop::loop, ev_io_watcher);

    cout << "libevent loop" << endl;

    ev_run(EventLoop::loop, 0);

    ev_loop_destroy(EventLoop::loop);
    free(ev_io_watcher);

    return (0);
}

void EventLoop::accept_cb(struct ev_loop *loop, ev_io *w, int revents) {
//    struct sockaddr_in clientaddr;
//    socklen_t socklen = sizeof(struct sockaddr_in);
//    int newfd = accept(w->fd, (struct sockaddr*) &clientaddr, &socklen);
//    if (newfd <= 0) {
//        return;
//    }
//
//    printf("Client connected! fd = [%d] ip = [%s] port=[%d]\n", newfd,
//            inet_ntoa(clientaddr.sin_addr), htons(clientaddr.sin_port));
//
//    cout << "Client connected! fd = [" << newfd << "] ip = ["
//            << inet_ntoa(clientaddr.sin_addr) << "] port = ["
//            << htons(clientaddr.sin_port) << "]" << endl;
//
//    EventLoop::setnonblock(newfd);
//
//    EventLoop::ioarray[newfd].io = (ev_io*) malloc(sizeof(ev_io));
//    EventLoop::ioarray[newfd].lasttime = ev_time();
//
//    ev_io_init(EventLoop::ioarray[newfd].io, recv_cb, newfd, EV_READ);
//    ev_io_start(loop, EventLoop::ioarray[newfd].io);

    return;
}

void EventLoop::recv_cb(struct ev_loop *loop, ev_io *w, int revents) {
#define BUFFER_SIZE 1024
#define INT_SIZEOF  sizeof(int)

    if (revents & EV_WRITE) {
        // TODO:
    }
    if (revents & EV_READ) {
        sleep(1);
        int fd = w->fd;
//        std::cout << "recv_cb - w->fd: " << w->fd << std::endl;

        char buffer[BUFFER_SIZE] = { 0 };
        memset(buffer, 0x00, sizeof(buffer));

        int recvbytes;
        size_t head_len;
        char recv_pm_len_arr[64] = { 0 };
        recvbytes = recv(fd, recv_pm_len_arr, INT_SIZEOF, 0);
        if (recvbytes <= 0) {
            if (0 == recvbytes) {
                // an orderly disconnect
                puts("HEAD: orderly disconnect\n");
                ::close(fd);
            } else if (EAGAIN == errno) {
                puts("BODY: should never get in this state with libev\n");
            } else {
                perror("recv");
            }
            return;
        }

//        std::cout << "recvbytes: " << recvbytes << std::endl;
        recv_pm_len_arr[recvbytes] = 0x00;
        head_len = htonl(*(int*) recv_pm_len_arr);
//        cout << "recv_pm_len: " << head_len << endl;

        (void) memset(buffer, 0x00, sizeof(buffer));
        recvbytes = recv(fd, buffer, head_len, 0);
        if (recvbytes <= 0) {
            if (0 == recvbytes) {
                // an orderly disconnect
                puts("HEAD: orderly disconnect\n");
                ::close(fd);
            } else if (EAGAIN == errno) {
                puts("BODY: should never get in this state with libev\n");
            } else {
                perror("recv");
            }
            return;
        }
        buffer[recvbytes] = '\0';

        string recv_str = buffer;
        ProtocolMessage* ppm = new ProtocolMessage();
//        std::cout << "recv_str: " << recv_str << endl;
        ppm->ParseFromString(recv_str);

        std::cout << "type:" << ppm->type() << endl;
        MESSAGEQUEUE->recvQueue.enqueue(ppm);

    }
    return;
}

void EventLoop::setnonblock(int fd) {
    fcntl(fd, F_SETFL, fcntl(fd, F_GETFL, 0) | O_NONBLOCK);
}

void EventLoop::setreuseaddr(int fd) {
    int reuse = 1;
    setsockopt(fd, SOL_SOCKET, SO_REUSEADDR, &reuse, sizeof(reuse));
}

void EventLoop::setnodelay(int fd) {
    int nodelay = 1;
    setsockopt(fd, IPPROTO_TCP, TCP_NODELAY, &nodelay, sizeof(nodelay));
}

void EventLoop::closefd(int fd) {
    struct sockaddr_in remote_addr;
    socklen_t socklen = sizeof(struct sockaddr_in);
    getpeername(fd, (struct sockaddr*) &remote_addr, &socklen);

    printf("Client disconnected! fd = [%d] ip = [%s] port=[%d]\n", fd,
            inet_ntoa(remote_addr.sin_addr), htons(remote_addr.sin_port));

    cout << "Client disconnected! fd = [" << fd << "] ip = ["
            << inet_ntoa(remote_addr.sin_addr) << "] port = ["
            << htons(remote_addr.sin_port) << "]" << endl;

    close(fd);
//    ev_io_stop(loop, EventLoop::ioarray[fd].io);
//    free(EventLoop::ioarray[fd].io);
//    EventLoop::ioarray[fd].io = NULL;
}

void EventLoop::time_cb(struct ev_loop* loop, struct ev_timer *timer,
        int revents) {
//    ev_tstamp now = ev_time();
//    for (register int i = 0; i < MAXFD; ++i) {
//        if (ioarray[i].io != NULL) {
//            //检测超时断开
//            if (TIMEOUT < now - ioarray[i].lasttime) {
//                cout << i << " now: " << now << " last recv data:"
//                        << ioarray[i].lasttime;
//                //EventLoop::closefd (i);
//            }
//        }
//    }
//    return;
}
