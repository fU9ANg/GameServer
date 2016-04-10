/*
 * CNL_Utils.h
 *
 *  Created on: 2016年4月9日
 *      Author: newman
 */

#ifndef CNL_UTILS_H_
#define CNL_UTILS_H_

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <errno.h>

#ifdef __cplusplus
extern "C"{
#endif

#define error_exit(result, fmt, args ...) \
    do { \
        printf ("[%s:%d] - "fmt"\n", __FILE__, __LINE__, ##args); \
        exit (result); \
    } while (0)

#define error_return(result, fmt, args ...) \
    do { \
        printf ("[%s:%d] - "fmt"\n", __FILE__, __LINE__, ##args); \
        return (result); \
    } while (0)

extern ssize_t send_n (int fd, void* buf, ssize_t left, struct timeval* timeout);
extern int     send_v (int fd, void* buf, ssize_t left);

extern ssize_t recv_n (int fd, void* buf, ssize_t left, struct timeval* timeout);
extern int     recv_v (int fd, void* buf, ssize_t left);

#ifdef __cplusplus
}
#endif


#endif /* CNL_UTILS_H_ */
