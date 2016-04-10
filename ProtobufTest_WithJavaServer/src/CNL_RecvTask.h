/*
 * CNL_RecvTask.h
 *
 *  Created on: 2016年4月8日
 *      Author: newman
 */

#ifndef CNL_RECVTASK_H_
#define CNL_RECVTASK_H_

#include "CNL_Defines.h"
#include "CNL_Task.h"
#include "PROTOCOL.pb.h"

class RecvTask: public Task
{
    public:
         RecvTask ();
        ~RecvTask ();

    public:
        virtual int work ();
};

#endif /* CNL_RECVTASK_H_ */
