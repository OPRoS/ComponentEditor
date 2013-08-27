
#ifndef _<%=Component_Name=%>_COMPONENT_H
#define _<%=Component_Name=%>_COMPONENT_H
/*
 *  Generated sources by OPRoS Component Generator (OCG V2)
 *   
 */
#include <Component.h>
#include <InputDataPort.h>
#include <OutputDataPort.h>
#include <InputEventPort.h>
#include <OutputEventPort.h>
#include <Event.h>
#include <OPRoSTypes.h>


#include "dioProvided.h"
	

class <%=Component_Name=%>: public Component
	,public Idio
{
protected:
// data


//event


// method for provider
	
	Idio *ptrDio;


// method for required
	


public:
	<%=Component_Name=%>();
	<%=Component_Name=%>(const std::string &compId);
	virtual ~<%=Component_Name=%>();
	virtual void portSetup();

protected:
	virtual ReturnType onInitialize();
	virtual ReturnType onStart();
	virtual ReturnType onStop();
	virtual ReturnType onReset();
	virtual ReturnType onError();
	virtual ReturnType onRecover();
	virtual ReturnType onDestroy();

public:
	virtual ReturnType onEvent(Event *evt);
	virtual ReturnType onExecute();
	virtual ReturnType onUpdated();
	virtual ReturnType onPeriodChanged();

	virtual unsigned int GetCount();
	virtual unsigned int GetDigin();
	virtual bool GetInput(unsigned int aIndex);
	virtual void SetOutput(unsigned int aCount,unsigned int aDigout);
};

#endif

