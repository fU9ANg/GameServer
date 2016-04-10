/*
 * CNL_TestTask.h
 *
 *  Created on: 2016年4月10日
 *      Author: newman
 */

#ifndef CNL_TESTTASK_H_
#define CNL_TESTTASK_H_

#include "CNL_Task.h"

class TestTask: public Task {
public:
    TestTask();
    ~TestTask();

public:
    virtual int work();
};

#endif /* CNL_TESTTASK_H_ */
