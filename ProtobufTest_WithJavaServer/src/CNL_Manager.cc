/*
 * CNL_Manager.cc
 *
 *  Created on: 2016年4月8日
 *      Author: newman
 */

#include "CNL_Manager.h"
#include "CNL_RecvTask.h"
#include "CNL_SendTask.h"
#include "CNL_EventLoop.h"
#include "CNL_TestTask.h"

manager::manager() {
    m_thrpool = new ThreadPool(4);
    m_lockfd = 0;
}

int manager::run() {
    printf("client running...\n");

    m_thrpool->start();
    EventLoop* evloop = new EventLoop("127.0.0.1", 8765);
    printf("server ip = [%s], port = [%d]\n", "127.0.0.1", 8765);
    RecvTask* precv = new RecvTask();
    SendTask* psend = new SendTask();
    TestTask* ptest = new TestTask();

    m_thrpool->push_task(evloop);   //监听和数据接收线程
    m_thrpool->push_task(precv);    //数据处理线程
    m_thrpool->push_task(psend);    //数据处理线程
    m_thrpool->push_task(ptest);

    std::cout << "client init success!" << endl;

    //主线程死循环
    while (true) {
        //cout<<"new client count = "<<evloop->getClientCount ()<<endl;
        std::cout << "Client loop ..." << std::endl;
        sleep(5);
    }

    std::cout << "Can't running here." << std::endl;
    m_thrpool->kill();
    return (0);
}

manager::~manager() {
    // TODO:
}
