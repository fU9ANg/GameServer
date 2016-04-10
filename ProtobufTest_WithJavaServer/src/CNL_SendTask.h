/*
 * CNL_SendTask.h
 *
 *  Created on: 2016年4月8日
 *      Author: newman
 */

#ifndef CNL_SENDTASK_H_
#define CNL_SENDTASK_H_

#include "CNL_Task.h"

class SendTask: public Task
{
    public:
         SendTask ();
        ~SendTask ();

    public:
        virtual int work ();
};


#endif /* CNL_SENDTASK_H_ */
