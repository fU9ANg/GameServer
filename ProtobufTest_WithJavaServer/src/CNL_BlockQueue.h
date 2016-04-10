/*
 * BlockQueue.h
 *
 *  Created on: 2016年4月1日
 *      Author: newman
 */

#ifndef	_BLOCKQUEUE_H
#define _BLOCKQUEUE_H

#include "CNL_Defines.h"

using namespace std;

template<typename type>
class BlockQueue {
public:
    BlockQueue() {
        pthread_mutex_init(&_mutex, NULL);
        pthread_cond_init(&_cond, NULL);
    }

    ~BlockQueue() {
        pthread_mutex_destroy(&_mutex);
        pthread_cond_destroy(&_cond);
    }

    int dequeue(type& data, unsigned int timeout) {
        struct timespec ts;
        pthread_mutex_lock(&_mutex);

        ts.tv_sec = time(NULL) + timeout;
        ts.tv_nsec = 0;

        while (_queue.empty()) {
            if (pthread_cond_timedwait(&_cond, &_mutex, &ts) != 0) {
                // wait timeout
                pthread_mutex_unlock(&_mutex);
                return (-1);
            }
        }

        // pop
        data = _queue.front();
        _queue.pop();

        pthread_mutex_unlock(&_mutex);

        return (0);
    }

    int enqueue(type data) {
        pthread_mutex_lock(&_mutex);

        // push
        _queue.push(data);
        pthread_mutex_unlock(&_mutex);

        if (_queue.empty()) {
            pthread_cond_signal(&_cond);
        }

        return (0);
    }

private:
    pthread_mutex_t _mutex;
    pthread_cond_t _cond;
    queue<type> _queue;
};

#endif	//_BLOCKQUEUE_H
