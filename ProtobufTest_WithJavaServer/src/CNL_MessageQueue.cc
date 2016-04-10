/*
 * MessageQueue.cc
 *
 *  Created on: 2016年4月7日
 *      Author: newman
 */

#include "CNL_MessageQueue.h"

MessageQueue* MessageQueue::p_instance = NULL;

MessageQueue* MessageQueue::instance() {
    if (p_instance == NULL) {
        p_instance = new MessageQueue();
    }
    return p_instance;
}

MessageQueue::MessageQueue()
{
}

MessageQueue::~MessageQueue()
{
}
