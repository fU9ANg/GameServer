/*
 * MessageQueue.h
 *
 *  Created on: 2016年4月7日
 *      Author: newman
 */

#ifndef CNL_MESSAGEQUEUE_H_
#define CNL_MESSAGEQUEUE_H_

#include "CNL_BlockQueue.h"
#include "PROTOCOL.pb.h"

using namespace Netty4;

class MessageQueue {
public:
    ~MessageQueue();
    static class MessageQueue* instance();
    BlockQueue<ProtocolMessage*> recvQueue;
    BlockQueue<ProtocolMessage*> sendQueue;
private:
    MessageQueue();
    static class MessageQueue* p_instance;
};

#define MESSAGEQUEUE MessageQueue::instance()

#endif /* CNL_MESSAGEQUEUE_H_ */
