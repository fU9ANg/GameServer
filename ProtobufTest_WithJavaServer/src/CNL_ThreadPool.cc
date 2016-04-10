/*
 * CNL_ThreadPool.cc
 *
 *  Created on: 2016年4月8日
 *      Author: newman
 */

#include "CNL_ThreadPool.h"

ThreadPool::ThreadPool (int n)
    : _free(0)
{
    count = n;
}

ThreadPool::~ThreadPool ()
{
    // TODO:
}

int ThreadPool::start ()
{
    _state = 1;
    for (int i = 0; i < count; ++i)
    {
        pthread_t tid = 0;
        pthread_create (&tid, 0, ThreadPool::thread, this);
        MutexLockGuard gard (_listLock);
        this->_threads.push_back (tid);
    }
    return (0);
}

int ThreadPool::stop ()
{
    _state = 0;
    while (this->getcount() != 0)
    {
        usleep (100);
    }
    return (0);
}

int ThreadPool::push_task (Task* p)
{
    return (this->_taskQueue.enqueue (p));
}

int ThreadPool::getcount ()
{
    MutexLockGuard gard (_listLock);
    return (this->_threads.size());
}

void* ThreadPool::thread (void* p)
{
    signal (SIGRTMIN, sighandle);
    ThreadPool* pp = static_cast <ThreadPool*> (p);
    Task* t = NULL;
    while (pp->_state.value () != 0)
    {
        if ((pp->_taskQueue.dequeue (t, 3) == 0) && (t != NULL))
        {
            pp->_free ++;
            t->work ();
            pp->_free --;
            if (t->autorelease)
            {
                delete t;
                t = NULL;
            }
        }
        else
        {
            t = NULL;
        }
    }
    MutexLockGuard gard (pp->_listLock);
    pp->_threads.remove (pthread_self ());
    return (NULL);
}

int ThreadPool::kill ()
{
    MutexLockGuard gard (this->_listLock);
    while (!_threads.empty ())
    {
        pthread_kill (_threads.front (), SIGRTMIN);
        _threads.pop_front ();
    }
    return (0);
}

void ThreadPool::sighandle (int signo)
{
    printf ("[%ld]exit\n", pthread_self ());
    pthread_exit (NULL);

    return;
}
