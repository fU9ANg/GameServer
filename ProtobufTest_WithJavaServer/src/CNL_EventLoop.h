/*
 * CNL_EventLoop.h
 *
 *  Created on: 2016年4月8日
 *      Author: newman
 */

#ifndef CNL_EVENTLOOP_H_
#define CNL_EVENTLOOP_H_

#include "CNL_Defines.h"
#include "CNL_Task.h"

#define MAXFD       10240
#define TIMEOUT     10

using namespace std;

//typedef struct ev_io_info {
//    struct ev_io* io;
//    ev_tstamp lasttime;
//
//} ev_in_info;

class EventLoop: public Task {
public:
    EventLoop(string ip, int port);
    ~EventLoop();
    virtual int work();

    static void accept_cb(struct ev_loop *loop, ev_io *w, int revents);
    static void recv_cb(struct ev_loop *loop, ev_io *w, int revents);
    static void time_cb(struct ev_loop *loop, struct ev_timer *timer,
            int revents);
    static void setnonblock(int fd);
    static void setreuseaddr(int fd);
    static void setnodelay(int fd);

    static void closefd(int fd);

    static struct ev_loop* loop;

//    static struct ev_io_info ioarray[MAXFD];

private:
    int recvbuf(int fd);
//    int startlisten();
//    int m_listenfd;
    int m_connectfd;
    int m_port;
    string m_ip;
};

#endif /* CNL_EVENTLOOP_H_ */
