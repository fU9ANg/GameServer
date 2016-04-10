################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CC_SRCS += \
../src/CNL_EventLoop.cc \
../src/CNL_Manager.cc \
../src/CNL_MessageQueue.cc \
../src/CNL_RecvTask.cc \
../src/CNL_SendTask.cc \
../src/CNL_TestTask.cc \
../src/CNL_ThreadPool.cc \
../src/CNL_Utils.cc \
../src/Main.cc \
../src/PROTOCOL.pb.cc \
../src/TEST.cc 

OBJS += \
./src/CNL_EventLoop.o \
./src/CNL_Manager.o \
./src/CNL_MessageQueue.o \
./src/CNL_RecvTask.o \
./src/CNL_SendTask.o \
./src/CNL_TestTask.o \
./src/CNL_ThreadPool.o \
./src/CNL_Utils.o \
./src/Main.o \
./src/PROTOCOL.pb.o \
./src/TEST.o 

CC_DEPS += \
./src/CNL_EventLoop.d \
./src/CNL_Manager.d \
./src/CNL_MessageQueue.d \
./src/CNL_RecvTask.d \
./src/CNL_SendTask.d \
./src/CNL_TestTask.d \
./src/CNL_ThreadPool.d \
./src/CNL_Utils.d \
./src/Main.d \
./src/PROTOCOL.pb.d \
./src/TEST.d 


# Each subdirectory must supply rules for building sources it contributes
src/%.o: ../src/%.cc
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C++ Compiler'
	g++ -O0 -g -Wall -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


