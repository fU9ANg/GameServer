/*
 * CNL_Task.h
 *
 *  Created on: 2016年4月8日
 *      Author: newman
 */

#ifndef CNL_TASK_H_
#define CNL_TASK_H_

#include "CNL_Defines.h"
#include "PROTOCOL.pb.h"

using namespace Netty4;

#define CT_FLAGS 0
#define ST_FLAGS 1

class Task
{
    public:
        Task () : autorelease (true)
        {
        }

        virtual ~Task () {}
        virtual int work () = 0;
        bool autorelease;

    public:
        void debugProtocol (ProtocolMessage* p)
        {}

    private:
};



#endif /* CNL_TASK_H_ */
