/*
 * CNL_SendTask.cc
 *
 *  Created on: 2016年4月8日
 *      Author: newman
 */

#include "CNL_SendTask.h"
#include "CNL_MessageQueue.h"
#include "PROTOCOL.pb.h"

#define SEND_BUFFER_SIZE    1024
#define INT_SIZEOF          sizeof(int)

using namespace Netty4;

int SendTask::work() {

    // FIXME: get fd.
    int fd = 3;

    while (true) {
        ProtocolMessage* ppm = NULL;

        if (MESSAGEQUEUE->sendQueue.dequeue(ppm, 3) != 0) {
            continue;
        }

        char buffer[SEND_BUFFER_SIZE] = { 0 };
        (void) memset(buffer, 0x00, sizeof(buffer));
        ppm->SerializePartialToArray(buffer, sizeof(buffer));
        size_t buffer_len = strlen(buffer);
        size_t bl = htonl(buffer_len);

//        cout << "send_protocol_message - int length: " << *(size_t*) &bl
//                << ", buffer_len: " << buffer_len << endl;
        int writen = write(fd, (const void*) &bl, INT_SIZEOF);
//        cout << "writen1: " << writen << endl;
        writen = write(fd, buffer, strlen(buffer));

//        cout << "writen2: " << writen << endl;

        if (ppm != NULL) {
            delete ppm;
            ppm = NULL;
        }
    }

    return (0);
}

SendTask::SendTask() {
    // TODO:
}

SendTask::~SendTask() {
    // TODO:
}

