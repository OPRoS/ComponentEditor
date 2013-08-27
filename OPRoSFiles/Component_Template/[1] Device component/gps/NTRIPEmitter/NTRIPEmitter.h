
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


#include "gpsProvided.h"
	

class <%=Component_Name=%>: public Component
	,public Igps
{
protected:
// data


//event


// method for provider
	
	Igps *ptrGps;


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

	virtual double GetLatitude();
	virtual double GetLongitude();
	virtual double GetAltitude();
	virtual unsigned int GetSatellites();
	virtual unsigned int GetQuality();
	virtual double GetHdop();
	virtual double GetVdop();
	virtual double GetUtmEasting();
	virtual double GetUtmNorthing();
	virtual double GetTime();
	virtual double GetErrHorizontal();
	virtual double GetErrVertical();
};

#endif

