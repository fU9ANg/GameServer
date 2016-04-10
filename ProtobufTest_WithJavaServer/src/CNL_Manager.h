/*
 * CNL_Manager.h
 *
 *  Created on: 2016年4月8日
 *      Author: newman
 */

#ifndef CNL_MANAGER_H_
#define CNL_MANAGER_H_

#include "CNL_Defines.h"
#include "CNL_ThreadPool.h"

enum {
    LOCK_WAIT = LOCK_EX, /* 阻塞锁 */
    LOCK_NOWAIT = LOCK_EX | LOCK_NB, /* 非阻塞锁 */
    UNLOCK = LOCK_UN /* 解锁 */
};

class manager {
public:
    manager();
    ~manager();

    int execute();
    int run();
    static void sig_term(int signo);

private:
    bool lock(int mode);
    ThreadPool* m_thrpool;
    int m_lockfd; /**文件锁句柄*/
};

#endif /* CNL_MANAGER_H_ */
