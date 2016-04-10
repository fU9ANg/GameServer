/*
 * CNL_Utils.cc
 *
 *  Created on: 2016年4月9日
 *      Author: newman
 */

#include "CNL_Utils.h"

ssize_t send_n (int fd, void* buf, ssize_t left, struct timeval* timeout)
{
    ssize_t recved = 0;
    fd_set  st;

    if (NULL == buf || NULL == timeout)
    {
        return (-1);
    }

    while (left != 0)
    {

        FD_ZERO (&st);
        FD_SET  (fd, &st);
        int i = select (fd + 1, NULL, &st, NULL, timeout);
        if (0 >= i) {
            break;
        } else {
            ssize_t len = send (fd, buf + recved, left, 0);
            if (0 == len) {
                break;
            }
            if (-1 == len) {
                return (-1);
            }
            left -= len;
            recved += len;
        }
    }
    return (0);
}

ssize_t recv_n (int fd, void* buf, ssize_t left, struct timeval* timeout)
{
    ssize_t recved = 0;
    fd_set  st;

    if (NULL == buf || NULL == timeout) {
        return (-1);
    }

    while (0 != left) {

        FD_ZERO (&st);
        FD_SET  (fd, &st);
        int i = select (fd + 1, &st, NULL, NULL, timeout);
        if (0 >= i) {
            break;
        } else {
            ssize_t len = recv (fd, buf + recved, left, 0);
            if (0 == len) {
                break;
            }
            if (-1 == len) {
                return (-1);
            }
            left -= len;
            recved += len;
        }
    }
    return (recved);
}

int send_v (int fd, void* buf, ssize_t left)
{
    int len    = 0;
    int sended = 0;

    if (NULL == buf) {
        return (-1);
    }

    while(0 != left) {
            len = send (fd, buf + sended, left, 0);

            if ((errno == EINTR) || (errno == EAGAIN)) {
                usleep (100);
                continue;
            }
            if (0 == len) {
                break;
            }
            if (-1 == len) {
                return (-1);
            }
            left -= len;
            sended += len;
    }
    return (sended);
}

int recv_v (int fd, void* buf, ssize_t left)
{
    int len    = 0;
    int recved = 0;

    if (NULL == buf) {
        return (-1);
    }

    while (0 != left) {
            len = recv (fd, (char*) buf + recved, left, 0);

            if ((errno == EINTR) || (errno == EAGAIN)) {
                usleep (100);
                continue;
            }
            if (0 == len) {
                break;
            }
            if (-1 == len) {
                return -1;
            }

            left -= len;
            recved += len;
    }
    return (recved);
}
