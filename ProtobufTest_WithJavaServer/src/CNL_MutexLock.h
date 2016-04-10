/*
 * MutexLock.h
 *
 *  Created on: 2016年4月7日
 *      Author: newman
 */

#ifndef CNL_MUTEXLOCK_H_
#define CNL_MUTEXLOCK_H_

#include "CNL_Defines.h"

class MutexLock {
public:
    MutexLock() {
        pthread_mutex_init(&_mutex, NULL);
    }
    ~MutexLock() {
        pthread_mutex_destroy(&_mutex);
    }
    void lock() {
        pthread_mutex_lock(&_mutex);
    }
    void unlock() {
        pthread_mutex_unlock(&_mutex);
    }
    pthread_mutex_t* getMutex() {
        return (&_mutex);
    }

private:
    MutexLock(const MutexLock&);
    const MutexLock& operator=(const MutexLock&);
    pthread_mutex_t _mutex;
};

class MutexLockGuard {
public:
    explicit MutexLockGuard(MutexLock& mutexLock) :
            _mutexLock(mutexLock) {
        _mutexLock.lock();
    }

    ~MutexLockGuard() {
        _mutexLock.unlock();
    }

private:
    MutexLockGuard(const MutexLockGuard&);
    const MutexLockGuard& operator=(const MutexLockGuard&);
    MutexLock& _mutexLock;
};

class Condition {
public:
    Condition(MutexLock& mutexLock) :
            _mutexLock(mutexLock) {
        pthread_cond_init(&_cond, NULL);
    }

    ~Condition() {
        pthread_cond_destroy(&_cond);
    }

    int wait() {
        pthread_cond_wait(&_cond, _mutexLock.getMutex());
        return (0);
    }

    int wait(time_t t) {
        struct timespec ts;
        ts.tv_sec = time(NULL) + t;
        ts.tv_nsec = 0;
        return (pthread_cond_timedwait(&_cond, _mutexLock.getMutex(), &ts));
    }

    void notify() {
        pthread_cond_signal(&_cond);
    }

    void notifyAll() {
        pthread_cond_broadcast(&_cond);
    }

private:
    Condition(const Condition&);
    MutexLock& _mutexLock;
    pthread_cond_t _cond;
};

#define MutexLockGuard(x) static_assert (false, "missing mutex guard variable name")

#endif /* CNL_MUTEXLOCK_H_ */
