/*
 * CNL_ThreadPool.h
 *
 *  Created on: 2016年4月8日
 *      Author: newman
 */

#ifndef CNL_THREADPOOL_H_
#define CNL_THREADPOOL_H_

#include "CNL_Defines.h"
#include "CNL_Task.h"
#include "CNL_MutexLock.h"
#include "CNL_Atomic.h"
#include "CNL_BlockQueue.h"

class ThreadPool
{
    public:
        ThreadPool (int n);
        ~ThreadPool ();
        int start ();
        int stop ();
        int kill ();
        int push_task (Task* p);
        static void* thread (void* p);
        static void sighandle (int signo);
        int getcount ();
        int getfree ();

    private:
        list <pthread_t> _threads;
        BlockQueue <Task*> _taskQueue;
        Atomic <int> _state;
        Atomic <int> _free;
        int count;
        MutexLock _listLock;
};

#endif /* CNL_THREADPOOL_H_ */
