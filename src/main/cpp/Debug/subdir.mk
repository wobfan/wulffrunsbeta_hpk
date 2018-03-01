################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
../Convergence.cpp \
../Differentiator.cpp \
../Integrator.cpp \
../TestFunctions.cpp \
../cDiffIntegrate.cpp \
../de_lab4inf_wrb_Differentiator.cpp \
../de_lab4inf_wrb_Integrator.cpp 

C_SRCS += \
../CTest.c \
../CUnit.c 

OBJS += \
./CTest.o \
./CUnit.o \
./Convergence.o \
./Differentiator.o \
./Integrator.o \
./TestFunctions.o \
./cDiffIntegrate.o \
./de_lab4inf_wrb_Differentiator.o \
./de_lab4inf_wrb_Integrator.o 

CPP_DEPS += \
./Convergence.d \
./Differentiator.d \
./Integrator.d \
./TestFunctions.d \
./cDiffIntegrate.d \
./de_lab4inf_wrb_Differentiator.d \
./de_lab4inf_wrb_Integrator.d 

C_DEPS += \
./CTest.d \
./CUnit.d 


# Each subdirectory must supply rules for building sources it contributes
%.o: ../%.c
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C Compiler'
	g++ -O0 -g3 -Wall -c -fmessage-length=0 -fPIC -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '

%.o: ../%.cpp
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C++ Compiler'
	g++ -I/usr/lib/jvm/default-java/include/linux -I/usr/lib/jvm/default-java/include -I/usr/lib/jvm/default/include/linux -I/usr/lib/jvm/default/include -O0 -g3 -Wall -c -fmessage-length=0 -fPIC -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


