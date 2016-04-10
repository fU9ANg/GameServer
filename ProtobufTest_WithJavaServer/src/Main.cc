/*
 * Main2.cc
 *
 *  Created on: 2016年4月8日
 *      Author: newman
 */

#include "PROTOCOL.pb.h"
#include "CNL_MessageQueue.h"
#include "CNL_Manager.h"
#include "CNL_EventLoop.h"

using namespace std;
using namespace Netty4;

int main() {
//    manager* process = NULL;
//
//    if ((process = new manager()) == NULL) {
//        cout << "ERROR: manager is NULL" << endl;
//        abort();
//    }
//
//    process->execute();
//
//    delete process;
//
//    ProtocolMessage pm = new ProtocolMessage();
//    pm.set_type(CMT_C_LOGIN);
//    ReqLogin rl;
//    rl.set_id(10);
//    rl.set_token("C++ token");
//    rl.set_platform("android v4.3");
//    rl.set_app_version("v1.3");
//    pm.set_allocated_req_login(&rl);
//    MESSAGEQUEUE->sendQueue.enqueue(&pm);


    // EventLoop el("127.0.0.1", 8765);
    // el.work();

    manager m;
    m.run();
}
