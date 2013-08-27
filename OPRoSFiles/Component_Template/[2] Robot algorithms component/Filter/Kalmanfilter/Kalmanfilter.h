
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


#include "BayesianFilteringProvided.h"
	

class <%=Component_Name=%>: public Component
	,public IBayesianFiltering
{
protected:
// data


//event


// method for provider
	
	IBayesianFiltering *ptrFilter;


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

	virtual Probability ProbabilityGet(const MatrixWrapper::ColumnVector& input);
	virtual MatrixWrapper::ColumnVector ExpectedValueGet();
	virtual MatrixWrapper::SymmetricMatrix CovarianceGet();
	virtual void ExpectedValueSet(const MatrixWrapper::ColumnVector& mu);
	virtual void CovarianceSet(const MatrixWrapper::SymmetricMatrix& cov);
	virtual bool NormalizeProbs();
	virtual bool CumPDFUpdate();
	virtual unsinged int NumStatesGet();
	virtual bool ProbabilitySet(int state,Probability a);
	virtual int MostProbableStateGet();
	virtual void PostSigmaSet(const MatrixWrapper::SymmetricMatrix& s);
	virtual void PostMuSet(const MatrixWrapper::ColumnVector& c);
	virtual void CalculateSysUpdate(const MatrixWrapper::ColumnVector& j,const MatrixWrapper::Matrix& F,const MatrixWrapper::SymmetricMatrix& q);
	virtual void SysUpdate(SystemModel<MatrixWrapper::ColumnVector>* const sysmodel,const MatrixWrapper::ColumnVector& u);
	virtual void MeasUpdate(MeasurementModel<MatrixWrapper::ColumnVector,MatrixWrapper::ColumnVector>* const measmodel,const MatrixWrapper::ColumnVector& z,const MatrixWrapper::ColumnVector& s);
	virtual bool UpdateInternal(SystemModel<MatrixWrapper::ColumnVector>* const sysmodel,const MatrixWrapper::ColumnVector& u,MeasurementModel<MatrixWrapper::ColumnVector,MatrixWrapper::ColumnVector>* const measmodel,const MatrixWrapper::ColumnVector& z,const MatrixWrapper::ColumnVector& s);
	virtual bool ProposalStepInternal(SystemModel<ColumnVector> * const sysmodel,const ColumnVector & u,MeasurementModel<ColumnVector,ColumnVector> * const measmodel,const ColumnVector & z,const ColumnVector & s);
	virtual bool Resample();
	virtual int IndexGet(const int& input,const std::vector<int>& condargs);
	virtual void AllocateMeasModelIExt(const vector<unsigned int>& meas_dimensions);
	virtual const MatrixWrapper::Matrix& AGet();
	virtual const MatrixWrapper::Matrix& BGet();
	virtual void ASet(const MatrixWrapper::Matrix & a);
	virtual void SetASIRFilter(MCPdf<StateVar> * prior,int resampleperiod,double resamplethreshold,int resamplescheme);
	virtual void SetBootstrapFilter(MCPdf<StateVar> * prior,int resampleperiod,double resamplethreshold,int resamplescheme);
	virtual void SetFilter(MCPdf<ColumnVector> * prior,int resampleperiod,double resamplethreshold,int resamplescheme);
	virtual bool UpdateWeightsInternal(SystemModel<StateVar> * const sysmodel,const StateVar & u,MeasurementModel<MeasVar,StateVar> * measmodel,const MeasVar & z,const StateVar & s);
	virtual bool DynamicResampleStep();
	virtual bool StaticResampleStep();
	virtual MCPdf<StateVar> * PostGet();
	virtual ConditionalPdf<StateVar,StateVar> * ProposalGet();
	virtual void ProposalSet(ConditionalPdf<StateVar,StateVar>* const cpdf);
	virtual void ParticleFilter(const ParticleFilter<StateVar,MeasVar> & filt);
	virtual void SRCovarianceSet(MatrixWrapper::Matrix JP_new);
	virtual void PriorSet(MatrixWrapper::ColumnVector& X,MatrixWrapper::SymmetricMatrix& P);
	virtual void CalculateMeasUpdate(MatrixWrapper::ColumnVector z,MatrixWrapper::ColumnVector Z,MatrixWrapper::Matrix H,MatrixWrapper::SymmetricMatrix R);
	virtual void CalculateMatrix(MatrixWrapper::Matrix& H_i,MatrixWrapper::SymmetricMatrix& R_i,MatrixWrapper::Matrix& invS,MatrixWrapper::Matrix& Sr,MatrixWrapper::Matrix& K_i);
	virtual void CalculateMean(MatrixWrapper::ColumnVector& x_k,const MatrixWrapper::ColumnVector& z,MatrixWrapper::ColumnVector& Z_i,MatrixWrapper::Matrix& K_i);
	virtual void CalculateCovariance(MatrixWrapper::Matrix& R_vf,MatrixWrapper::Matrix& H_i,MatrixWrapper::Matrix& invS,MatrixWrapper::Matrix& SR);
};

#endif

