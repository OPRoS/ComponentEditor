
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


#include "BayesianFilteringProvided.h"
	

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

	// provided service port setup
	
	ProvidedServicePort *pa1;
	pa1=new BayesianFilteringProvided(this);

	addPort("filter",pa1);

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


Probability <%=Component_Name=%>::ProbabilityGet(const MatrixWrapper::ColumnVector& input)
{
	// user code here
}
MatrixWrapper::ColumnVector <%=Component_Name=%>::ExpectedValueGet()
{
	// user code here
}
MatrixWrapper::SymmetricMatrix <%=Component_Name=%>::CovarianceGet()
{
	// user code here
}
void <%=Component_Name=%>::ExpectedValueSet(const MatrixWrapper::ColumnVector& mu)
{
	// user code here
}
void <%=Component_Name=%>::CovarianceSet(const MatrixWrapper::SymmetricMatrix& cov)
{
	// user code here
}
bool <%=Component_Name=%>::NormalizeProbs()
{
	// user code here
}
bool <%=Component_Name=%>::CumPDFUpdate()
{
	// user code here
}
unsinged int <%=Component_Name=%>::NumStatesGet()
{
	// user code here
}
bool <%=Component_Name=%>::ProbabilitySet(int state,Probability a)
{
	// user code here
}
int <%=Component_Name=%>::MostProbableStateGet()
{
	// user code here
}
void <%=Component_Name=%>::PostSigmaSet(const MatrixWrapper::SymmetricMatrix& s)
{
	// user code here
}
void <%=Component_Name=%>::PostMuSet(const MatrixWrapper::ColumnVector& c)
{
	// user code here
}
void <%=Component_Name=%>::CalculateSysUpdate(const MatrixWrapper::ColumnVector& j,const MatrixWrapper::Matrix& F,const MatrixWrapper::SymmetricMatrix& q)
{
	// user code here
}
void <%=Component_Name=%>::SysUpdate(SystemModel<MatrixWrapper::ColumnVector>* const sysmodel,const MatrixWrapper::ColumnVector& u)
{
	// user code here
}
void <%=Component_Name=%>::MeasUpdate(MeasurementModel<MatrixWrapper::ColumnVector,MatrixWrapper::ColumnVector>* const measmodel,const MatrixWrapper::ColumnVector& z,const MatrixWrapper::ColumnVector& s)
{
	// user code here
}
bool <%=Component_Name=%>::UpdateInternal(SystemModel<MatrixWrapper::ColumnVector>* const sysmodel,const MatrixWrapper::ColumnVector& u,MeasurementModel<MatrixWrapper::ColumnVector,MatrixWrapper::ColumnVector>* const measmodel,const MatrixWrapper::ColumnVector& z,const MatrixWrapper::ColumnVector& s)
{
	// user code here
}
bool <%=Component_Name=%>::ProposalStepInternal(SystemModel<ColumnVector> * const sysmodel,const ColumnVector & u,MeasurementModel<ColumnVector,ColumnVector> * const measmodel,const ColumnVector & z,const ColumnVector & s)
{
	// user code here
}
bool <%=Component_Name=%>::Resample()
{
	// user code here
}
int <%=Component_Name=%>::IndexGet(const int& input,const std::vector<int>& condargs)
{
	// user code here
}
void <%=Component_Name=%>::AllocateMeasModelIExt(const vector<unsigned int>& meas_dimensions)
{
	// user code here
}
const MatrixWrapper::Matrix& <%=Component_Name=%>::AGet()
{
	// user code here
}
const MatrixWrapper::Matrix& <%=Component_Name=%>::BGet()
{
	// user code here
}
void <%=Component_Name=%>::ASet(const MatrixWrapper::Matrix & a)
{
	// user code here
}
void <%=Component_Name=%>::SetASIRFilter(MCPdf<StateVar> * prior,int resampleperiod,double resamplethreshold,int resamplescheme)
{
	// user code here
}
void <%=Component_Name=%>::SetBootstrapFilter(MCPdf<StateVar> * prior,int resampleperiod,double resamplethreshold,int resamplescheme)
{
	// user code here
}
void <%=Component_Name=%>::SetFilter(MCPdf<ColumnVector> * prior,int resampleperiod,double resamplethreshold,int resamplescheme)
{
	// user code here
}
bool <%=Component_Name=%>::UpdateWeightsInternal(SystemModel<StateVar> * const sysmodel,const StateVar & u,MeasurementModel<MeasVar,StateVar> * measmodel,const MeasVar & z,const StateVar & s)
{
	// user code here
}
bool <%=Component_Name=%>::DynamicResampleStep()
{
	// user code here
}
bool <%=Component_Name=%>::StaticResampleStep()
{
	// user code here
}
MCPdf<StateVar> * <%=Component_Name=%>::PostGet()
{
	// user code here
}
ConditionalPdf<StateVar,StateVar> * <%=Component_Name=%>::ProposalGet()
{
	// user code here
}
void <%=Component_Name=%>::ProposalSet(ConditionalPdf<StateVar,StateVar>* const cpdf)
{
	// user code here
}
void <%=Component_Name=%>::ParticleFilter(const ParticleFilter<StateVar,MeasVar> & filt)
{
	// user code here
}
void <%=Component_Name=%>::SRCovarianceSet(MatrixWrapper::Matrix JP_new)
{
	// user code here
}
void <%=Component_Name=%>::PriorSet(MatrixWrapper::ColumnVector& X,MatrixWrapper::SymmetricMatrix& P)
{
	// user code here
}
void <%=Component_Name=%>::CalculateMeasUpdate(MatrixWrapper::ColumnVector z,MatrixWrapper::ColumnVector Z,MatrixWrapper::Matrix H,MatrixWrapper::SymmetricMatrix R)
{
	// user code here
}
void <%=Component_Name=%>::CalculateMatrix(MatrixWrapper::Matrix& H_i,MatrixWrapper::SymmetricMatrix& R_i,MatrixWrapper::Matrix& invS,MatrixWrapper::Matrix& Sr,MatrixWrapper::Matrix& K_i)
{
	// user code here
}
void <%=Component_Name=%>::CalculateMean(MatrixWrapper::ColumnVector& x_k,const MatrixWrapper::ColumnVector& z,MatrixWrapper::ColumnVector& Z_i,MatrixWrapper::Matrix& K_i)
{
	// user code here
}
void <%=Component_Name=%>::CalculateCovariance(MatrixWrapper::Matrix& R_vf,MatrixWrapper::Matrix& H_i,MatrixWrapper::Matrix& invS,MatrixWrapper::Matrix& SR)
{
	// user code here
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

