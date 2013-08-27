
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


#include "imuProvided.h"
	

class <%=Component_Name=%>: public Component
	,public Iimu
{
protected:
// data


//event


// method for provider
	
	Iimu *ptrImu;


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

	virtual double GetXAccel();
	virtual double GetYAccel();
	virtual double GetZAccel();
	virtual double GetXGyro();
	virtual double GetYGyro();
	virtual double GetZGyro();
	virtual double GetXMagn();
	virtual double GetYMagn();
	virtual double GetZMagn();
	virtual void SetDatatype(int aDatatype);
	virtual void ResetOrientation(int aValue);
	virtual opros_pose3d GetPose(unsigned int aIndex);
	virtual void RequestGeom();
};

#endif

