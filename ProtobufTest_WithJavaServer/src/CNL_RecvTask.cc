/*
 * CNL_RecvTask.cc
 *
 *  Created on: 2016年4月8日
 *      Author: newman
 */

#include "CNL_Defines.h"
#include "CNL_RecvTask.h"
#include "CNL_MessageQueue.h"
#include "PROTOCOL.pb.h"

using namespace std;
using namespace Netty4;

int RecvTask::work() {

    while (true) {

        ProtocolMessage* ppm = NULL;
        if (MESSAGEQUEUE->recvQueue.dequeue(ppm, 3) != 0) {
            continue;
        }

        if (ppm == NULL) {
            cout << "ERROR: ppm==NULL in RecvTask::work()" << endl;
            return (0);
        }

        switch (ppm->type()) {
        case CMT_S_LOGIN:
            cout << "===== DEBUG FOR `CMT_S_LOGIN` =====" << endl;
            cout << "rlt_code: " << ppm->resp_login().rlt().code() << endl;
            cout << "rlt_msg: " << ppm->resp_login().rlt().msg() << endl;
            break;
        case CMT_S_PPTLIST:
            cout << "===== DEBUG FOR `CMT_S_PPTLIST` =====" << endl;
            cout << "rlt_code: " << ppm->resp_ppt_list().rlt().code() << endl;
            cout << "rlt_msg: " << ppm->resp_ppt_list().rlt().msg() << endl;
            cout << "tag: " << ppm->resp_ppt_list().tag() << endl;
            cout << "desc: " << ppm->resp_ppt_list().desc() << endl;
            cout << "node: " << endl;
            for (int i = 0; i < ppm->resp_ppt_list().node_size(); ++i) {
                cout << "\tnode" << i << " - id : "
                        << ppm->resp_ppt_list().node(i).id() << endl;
                cout << "\tnode" << i << " - image : "
                        << ppm->resp_ppt_list().node(i).image() << endl;
                cout << "\tnode" << i << " - order : "
                        << ppm->resp_ppt_list().node(i).order() << endl;
            }
            break;
        case CMT_C_LOGIN:
            cout << "===== DEBUG FOR `CMT_C_LOGIN` =====" << endl;
            cout << "id: " << ppm->req_login().id() << endl;
            cout << "token: " << ppm->req_login().token() << endl;
            cout << "app version: " << ppm->req_login().app_version() << endl;
            cout << "platform: " << ppm->req_login().platform() << endl;
            break;

        case CMT_C_PPTLIST:
            cout << "===== DEBUG FOR `CMT_C_PPTLIST` =====" << endl;
            cout << "userid: " << ppm->req_ppt_list().userid() << endl;
            cout << "vspid: " << ppm->req_ppt_list().vspid() << endl;
            break;

        default:
            cout << "Don't know message type!" << endl;
            break;
        }

    }
    return (0);
}

RecvTask::RecvTask() {
}

RecvTask::~RecvTask() {
}
