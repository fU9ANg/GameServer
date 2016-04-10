/*
 * CNL_TestTask.cc
 *
 *  Created on: 2016年4月10日
 *      Author: newman
 */

#include "CNL_Defines.h"
#include "PROTOCOL.pb.h"
#include "CNL_MessageQueue.h"
#include "CNL_TestTask.h"

#define SEND_BUFFER_SIZE    1024
#define INT_SIZEOF          sizeof(int)

using namespace std;
using namespace Netty4;

int TestTask::work() {

    while (1) {
        sleep(2);
        // Fill PPTList Protocol
        ProtocolMessage* pm_pptlist = new ProtocolMessage();
        ReqPPTList* pptlist = new ReqPPTList();
        pptlist->set_userid(1234);
        pptlist->set_vspid(4321);
        pm_pptlist->set_type(CMT_C_PPTLIST);
        pm_pptlist->set_allocated_req_ppt_list(pptlist);

//        cout << "pm_pptlist: " << pm_pptlist << endl;
        MESSAGEQUEUE->sendQueue.enqueue(pm_pptlist);

        sleep(2);

        // Fill Login Protocol
        ProtocolMessage* pm_login = new ProtocolMessage();
        ReqLogin* login = new ReqLogin();
        login->set_id(90);
        login->set_token("C++ SKDJFKSJDFKJSKDJF");
        login->set_app_version("v1.3");
        login->set_platform("android v4.5 versi的");
        pm_login->set_type(CMT_C_LOGIN);
        pm_login->set_allocated_req_login(login);

//        cout << "pm_login: " << pm_login << endl;
        MESSAGEQUEUE->sendQueue.enqueue(pm_login);
    }

    return (0);
}

TestTask::TestTask() {
    // TODO:
}

TestTask::~TestTask() {
    // TODO:
}
