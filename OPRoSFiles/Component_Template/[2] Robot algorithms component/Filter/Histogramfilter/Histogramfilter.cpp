
/*
 *  Generated sources by OPRoS Component Generator (OCG V2)
 *  
 */
#include <Component.h>
#include <InputDataPort.h>
#include <OutputDataPort.h>
#include <InputEventPort.h>
#include <OutputEventPort.h>
#include <OPRoSTypes.h>
#include <EventData.h>



#include "<%=Component_Name=%>.h"

//
// constructor declaration
//
<%=Component_Name=%>::<%=Component_Name=%>()
{

	
	portSetup();
}

//
// constructor declaration (with name)
//
<%=Component_Name=%>::<%=Component_Name=%>(const std::string &name):Component(name)
{

	
	portSetup();
}

//
// destructor declaration
//

<%=Component_Name=%>::~<%=Component_Name=%>() {
}

void <%=Component_Name=%>::portSetup() {

}

// Call back Declaration
ReturnType <%=Component_Name=%>::onInitialize()
{
	// user code here
	return OPROS_SUCCESS;
}

ReturnType <%=Component_Name=%>::onStart()
{
	// user code here
	return OPROS_SUCCESS;
}
	
ReturnType <%=Component_Name=%>::onStop()
{
	// user code here
	return OPROS_SUCCESS;
}

ReturnType <%=Component_Name=%>::onReset()
{
	// user code here
	return OPROS_SUCCESS;
}

ReturnType <%=Component_Name=%>::onError()
{
	// user code here
	return OPROS_SUCCESS;
}

ReturnType <%=Component_Name=%>::onRecover()
{
	// user code here
	return OPROS_SUCCESS;
}

ReturnType <%=Component_Name=%>::onDestroy()
{
	// user code here
	return OPROS_SUCCESS;
}

ReturnType <%=Component_Name=%>::onEvent(Event *evt)
{
	// user code here
	return OPROS_SUCCESS;
}

ReturnType <%=Component_Name=%>::onExecute()
{
	// user code here
	return OPROS_SUCCESS;
}
	
ReturnType <%=Component_Name=%>::onUpdated()
{
	// user code here
	return OPROS_SUCCESS;
}

ReturnType <%=Component_Name=%>::onPeriodChanged()
{
	// user code here
	return OPROS_SUCCESS;
}






#ifdef MAKE_COMPONENT_DLL
#ifdef WIN32

extern "C"
{
__declspec(dllexport) Component *getComponent();
__declspec(dllexport) void releaseComponent(Component *pcom);
}

Component *getComponent()
{
	return new <%=Component_Name=%>();
}

void releaseComponent(Component *com)
{
	delete com;
}


#else
extern "C"
{
	Component *getComponent();
	void releaseComponent(Component *com);
}
Component *getComponent()
{
	return new <%=Component_Name=%>();
}

void releaseComponent(Component *com)
{
	delete com;
}
#endif
#endif

