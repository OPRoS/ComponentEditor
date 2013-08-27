<!--
** Copyright (C) 2008 ETRI, All rights reserved
**  
**  
** This programs is the production of ETRI research & development activity;
** you cannot use it and cannot redistribute it and cannot modify it and
** cannnot  store it in any media(disk, photo or otherwise) without the prior
** permission of ETRI.
** 
** You should have received the license for this program to use any purpose.
** If not, contact the Electronics and Telecommunications Research Institute
** [ETRI], DaeDog DanJi, TaeJeon, 305-350, Korea.
** 
** NO PART OF THIS WORK BY THE THIS COPYRIGHT HEREON MAY BE REPRODUCED, STORED
** IN RETRIEVAL SYSTENS, IN ANY FORM OR BY ANY MEANS, ELECTRONIC, MECHANICAL,
** PHOTOCOPYING, RECORDING OR OTHERWISE, WITHOUT THE PRIOR PERMISSION OF ETRI
**
** @Author: sby (sby@etri.re.kr)
-->
<xsl:stylesheet version="1.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:output method = "text"/>
<xsl:param name="filename"/>
<xsl:param name="usage"/>
<!--
** Modify (C) 2008 ED Corp.
** @Author: sevensky(Juwon Kim) (jwkim@ed.co.kr)
-->
<xsl:param name="outpath"/>
<!--  -->
<xsl:template name="componentHeader"> <!-- match="component" -->
<xsl:param name="filename"/>
#ifndef _<xsl:value-of select="name"/>_COMPONENT_H
#define _<xsl:value-of select="name"/>_COMPONENT_H
/*
 *  Generated sources by OPRoS Component Generator (OCG V2)
 *   
 */
#include &lt;Component.h&gt;
#include &lt;InputDataPort.h&gt;
#include &lt;OutputDataPort.h&gt;
#include &lt;InputEventPort.h&gt;
#include &lt;OutputEventPort.h&gt;
#include &lt;Event.h&gt;
#include &lt;OPRoSTypes.h&gt;

<xsl:if test="ports/*/data_type">
<xsl:for-each select="ports/*">
	<xsl:variable name="data_type" select="data_type"/>
	<xsl:variable name="data_name" select="name"/>
	<xsl:choose>
	<xsl:when test="count(//ports/*[data_type=$data_type])=1">
		<xsl:choose>
		<xsl:when test="starts-with($data_type,'std::')">
		<xsl:choose>
		<xsl:when test="contains($data_type,'&lt;')">
#include &lt;<xsl:value-of select="normalize-space(substring-before(substring-after($data_type,'std::'),'&lt;'))"/>&gt;
		</xsl:when>
		<xsl:otherwise>
#include &lt;<xsl:value-of select="normalize-space(substring-after($data_type,'std::'))"/>&gt;
		</xsl:otherwise>
		</xsl:choose>
		</xsl:when>
		<xsl:when test="$data_type='int'"/>
		<xsl:when test="$data_type='short'"/>
		<xsl:when test="$data_type='long'"/>
		<xsl:when test="$data_type='char'"/>
		<xsl:when test="contains($data_type,'unsigned')"/>
		<xsl:when test="$data_type='bool'"/>
		<xsl:when test="$data_type='float'"/>
		<xsl:when test="$data_type='double'"/>
		<xsl:otherwise>
#include "<xsl:value-of select="$data_type"/>.h"
		</xsl:otherwise>
		</xsl:choose>
	</xsl:when>
	<xsl:otherwise>
		<xsl:variable name="nv" select="//ports/*[data_type=$data_type]/name"/>
		<xsl:if test="contains($nv,$data_name)">
		<xsl:choose>
		<xsl:when test="starts-with($data_type,'std::')">
		<xsl:choose>
		<xsl:when test="contains($data_type,'&lt;')">
#include &lt;<xsl:value-of select="normalize-space(substring-before(substring-after($data_type,'std::'),'&lt;'))"/>&gt;
		</xsl:when>
		<xsl:otherwise>
#include &lt;<xsl:value-of select="normalize-space(substring-after($data_type,'std::'))"/>&gt;
		</xsl:otherwise>
		</xsl:choose>
		</xsl:when>
		<xsl:when test="$data_type='int'"/>
		<xsl:when test="$data_type='short'"/>
		<xsl:when test="$data_type='long'"/>
		<xsl:when test="$data_type='char'"/>
		<xsl:when test="contains($data_type,'unsigned')"/>
		<xsl:when test="$data_type='bool'"/>
		<xsl:when test="$data_type='float'"/>
		<xsl:when test="$data_type='double'"/>
		<xsl:otherwise>
#include "<xsl:value-of select="$data_type"/>.h"
		</xsl:otherwise>
		</xsl:choose>
		</xsl:if>
	</xsl:otherwise>
	</xsl:choose>
</xsl:for-each>
</xsl:if>

<xsl:if test="ports/service_port">
<xsl:for-each select="ports/service_port">
	<xsl:choose>
	<xsl:when test="usage='required'">
#include "<xsl:value-of select="type"/>Required.h"
	</xsl:when>
	<xsl:otherwise>
#include "<xsl:value-of select="type"/>Provided.h"
	</xsl:otherwise>
	</xsl:choose>
</xsl:for-each>
</xsl:if>

class <xsl:value-of select="name"/>: public Component<xsl:for-each select="ports/service_port[usage='provided']">
	,public<xsl:text> </xsl:text>I<xsl:value-of select="type"/>
</xsl:for-each>
{
protected:
// data
<xsl:for-each select="ports/data_port">
<xsl:choose>
<xsl:when test="usage='input'">
	InputDataPort&lt; <xsl:value-of select="data_type"/> &gt; <xsl:value-of select="name"/>;</xsl:when>
<xsl:otherwise>
	OutputDataPort&lt; <xsl:value-of select="data_type"/> &gt; <xsl:value-of select="name"/>;</xsl:otherwise>
</xsl:choose>
</xsl:for-each>

//event
<xsl:for-each select="ports/event_port">
<xsl:choose>
<xsl:when test="usage='input'">
	InputEventPort&lt; <xsl:value-of select="data_type"/> &gt; <xsl:value-of select="name"/>;</xsl:when>
<xsl:otherwise>
	OutputEventPort&lt; <xsl:value-of select="data_type"/> &gt; <xsl:value-of select="name"/>;</xsl:otherwise>
</xsl:choose>
</xsl:for-each>

// method for provider
	<xsl:for-each select="ports/service_port[usage='provided']">
	I<xsl:value-of select="type"/><xsl:text> *ptr</xsl:text><xsl:value-of select="name"/>;
</xsl:for-each>

// method for required
	<xsl:for-each select="ports/service_port[usage='required']">
	<xsl:value-of select="type"/>Required<xsl:text> *ptr</xsl:text><xsl:value-of select="name"/>;
</xsl:for-each>


public:
	<xsl:value-of select="name"/>();
	<xsl:value-of select="name"/>(const std::string &amp;compId);
	virtual ~<xsl:value-of select="name"/>();
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
<xsl:for-each select="ports/service_port[usage='provided']">
<xsl:variable name="t" select="document(reference)"/>
<xsl:variable name="s_type" select="type"/>
<xsl:choose>
<xsl:when test="$t/service_port_type_profile/service_port_type/type_name=type">
<xsl:for-each select="$t/service_port_type_profile/service_port_type[type_name=$s_type]/method">
	virtual <xsl:value-of select="@return_type"/><xsl:text> </xsl:text><xsl:value-of select="@name"/>(<xsl:for-each select="param"><xsl:if test="position()!=1">,</xsl:if><xsl:value-of select="type"/><xsl:text> </xsl:text><xsl:value-of select="name"/></xsl:for-each>);</xsl:for-each>
</xsl:when>
<xsl:otherwise>
	&lt;error&gt; code generation error &lt;/error&gt;
	&lt;error&gt; Please Check Your Component Profile or Service Port Profile&lt;/error&gt; 
	&lt;error&gt; Profile name or profile type mismatch&lt;/error&gt; 
</xsl:otherwise>
</xsl:choose>
</xsl:for-each>
};

#endif

</xsl:template>
<!--
** Copyright (C) 2008 ETRI, All rights reserved
**  
**  
** This programs is the production of ETRI research & development activity;
** you cannot use it and cannot redistribute it and cannot modify it and
** cannnot  store it in any media(disk, photo or otherwise) without the prior
** permission of ETRI.
** 
** You should have received the license for this program to use any purpose.
** If not, contact the Electronics and Telecommunications Research Institute
** [ETRI], DaeDog DanJi, TaeJeon, 305-350, Korea.
** 
** NO PART OF THIS WORK BY THE THIS COPYRIGHT HEREON MAY BE REPRODUCED, STORED
** IN RETRIEVAL SYSTENS, IN ANY FORM OR BY ANY MEANS, ELECTRONIC, MECHANICAL,
** PHOTOCOPYING, RECORDING OR OTHERWISE, WITHOUT THE PRIOR PERMISSION OF ETRI
**
** @Author: sby (sby@etri.re.kr)
-->
<xsl:template name="componentSource" match="component">
<xsl:param name="filename"/>
/*
 *  Generated sources by OPRoS Component Generator (OCG V2)
 *  
 */
#include &lt;Component.h&gt;
#include &lt;InputDataPort.h&gt;
#include &lt;OutputDataPort.h&gt;
#include &lt;InputEventPort.h&gt;
#include &lt;OutputEventPort.h&gt;
#include &lt;OPRoSTypes.h&gt;
#include &lt;EventData.h&gt;

<xsl:if test="ports/service_port">
<xsl:for-each select="ports/service_port">
	<xsl:choose>
	<xsl:when test="usage='required'">
#include "<xsl:value-of select="type"/>Required.h"
	</xsl:when>
	<xsl:otherwise>
#include "<xsl:value-of select="type"/>Provided.h"
	</xsl:otherwise>
	</xsl:choose>
</xsl:for-each>
</xsl:if>

#include "<xsl:value-of select="$filename"/>.h"

//
// constructor declaration
//
<xsl:value-of select="name"/>::<xsl:value-of select="name"/>()<xsl:if test="ports/data_port"><xsl:if test="ports/data_port/usage='input'">
		:<xsl:for-each select="ports/data_port[usage='input']"><xsl:if test="position()!=1">,</xsl:if><xsl:value-of select="name"/>(<xsl:choose><xsl:when test="queueing_policy">OPROS_<xsl:value-of select="queueing_policy"/>,<xsl:value-of select="queue_size"/></xsl:when><xsl:otherwise>OPROS_LAST,1</xsl:otherwise></xsl:choose>)</xsl:for-each>
</xsl:if></xsl:if>
{
<xsl:for-each select="ports/service_port"><xsl:choose><xsl:when test="usage='provided'">
	</xsl:when><xsl:otherwise>	ptr<xsl:value-of select="name"/> = NULL;</xsl:otherwise></xsl:choose></xsl:for-each>
	
	portSetup();
}

//
// constructor declaration (with name)
//
<xsl:value-of select="name"/>::<xsl:value-of select="name"/>(const std::string &amp;name):Component(name)<xsl:if test="ports/data_port"><xsl:if test="ports/data_port/usage='input'">
		,<xsl:for-each select="ports/data_port[usage='input']"><xsl:if test="position()!=1">,</xsl:if><xsl:value-of select="name"/>(<xsl:choose><xsl:when test="queueing_policy">OPROS_<xsl:value-of select="queueing_policy"/>,<xsl:value-of select="queue_size"/></xsl:when><xsl:otherwise>OPROS_LAST,1</xsl:otherwise></xsl:choose>)</xsl:for-each>
</xsl:if></xsl:if>
{
<xsl:for-each select="ports/service_port"><xsl:choose><xsl:when test="usage='provided'">
	</xsl:when><xsl:otherwise>	ptr<xsl:value-of select="name"/> = NULL;</xsl:otherwise></xsl:choose></xsl:for-each>
	
	portSetup();
}

//
// destructor declaration
//

<xsl:value-of select="name"/>::~<xsl:value-of select="name"/>() {
}

void <xsl:value-of select="name"/>::portSetup() {
<xsl:if test="ports/service_port">
<xsl:if test="ports/service_port/usage='provided'">
	// provided service port setup
	<xsl:for-each select="ports/service_port[usage='provided']">
	ProvidedServicePort *pa<xsl:value-of select="position()"/>;
	pa<xsl:value-of select="position()"/>=new <xsl:value-of select="type"/>Provided(this);

	addPort("<xsl:value-of select="name"/>",pa<xsl:value-of select="position()"/>);
</xsl:for-each>
</xsl:if>
<xsl:if test="ports/service_port/usage='required'">
	// required service port setup
<xsl:for-each select="ports/service_port[usage='required']">
	ptr<xsl:value-of select="name"/>=new <xsl:value-of select="type"/>Required();
	addPort("<xsl:value-of select="name"/>",ptr<xsl:value-of select="name"/>);
</xsl:for-each>
</xsl:if>
</xsl:if>

<xsl:if test="ports/data_port">
	// data port setup
<xsl:for-each select="ports/data_port">
<xsl:choose>
<xsl:when test="usage='input'">
	addPort("<xsl:value-of select="name"/>", &amp;<xsl:value-of select="name"/>);
</xsl:when>
<xsl:otherwise>
	addPort("<xsl:value-of select="name"/>", &amp;<xsl:value-of select="name"/>);
</xsl:otherwise>
</xsl:choose>
</xsl:for-each>
</xsl:if>

<xsl:if test="ports/event_port">
	// event port setup
<xsl:for-each select="ports/event_port">
<xsl:choose>
<xsl:when test="usage='input'">
	addPort("<xsl:value-of select="name"/>", &amp;<xsl:value-of select="name"/>);
	<xsl:value-of select="name"/>.setOwner(this);
</xsl:when>
<xsl:otherwise>
	addPort("<xsl:value-of select="name"/>", &amp;<xsl:value-of select="name"/>);
</xsl:otherwise>
</xsl:choose>
</xsl:for-each>
</xsl:if>
}

// Call back Declaration
ReturnType <xsl:value-of select="name"/>::onInitialize()
{
	// user code here
	return OPROS_SUCCESS;
}

ReturnType <xsl:value-of select="name"/>::onStart()
{
	// user code here
	return OPROS_SUCCESS;
}
	
ReturnType <xsl:value-of select="name"/>::onStop()
{
	// user code here
	return OPROS_SUCCESS;
}

ReturnType <xsl:value-of select="name"/>::onReset()
{
	// user code here
	return OPROS_SUCCESS;
}

ReturnType <xsl:value-of select="name"/>::onError()
{
	// user code here
	return OPROS_SUCCESS;
}

ReturnType <xsl:value-of select="name"/>::onRecover()
{
	// user code here
	return OPROS_SUCCESS;
}

ReturnType <xsl:value-of select="name"/>::onDestroy()
{
	// user code here
	return OPROS_SUCCESS;
}

ReturnType <xsl:value-of select="name"/>::onEvent(Event *evt)
{
	// user code here
	return OPROS_SUCCESS;
}

ReturnType <xsl:value-of select="name"/>::onExecute()
{
	// user code here
	return OPROS_SUCCESS;
}
	
ReturnType <xsl:value-of select="name"/>::onUpdated()
{
	// user code here
	return OPROS_SUCCESS;
}

ReturnType <xsl:value-of select="name"/>::onPeriodChanged()
{
	// user code here
	return OPROS_SUCCESS;
}


<xsl:variable name="cname" select="name"/>
<xsl:for-each select="ports/service_port[usage='provided']">
<xsl:variable name="t" select="document(reference)"/>
<xsl:variable name="s_type" select="type"/>
<xsl:if test="$t/service_port_type_profile/service_port_type/type_name=type">
	<xsl:for-each select="$t/service_port_type_profile/service_port_type[type_name=$s_type]/method">
	<xsl:value-of select="@return_type"/><xsl:text> </xsl:text><xsl:value-of select="$cname"/>::<xsl:value-of select="@name"/>(<xsl:for-each select="param"><xsl:if test="position()!=1">,</xsl:if><xsl:value-of select="type"/><xsl:text> </xsl:text><xsl:value-of select="name"/></xsl:for-each>)
{
	// user code here
}
</xsl:for-each>
</xsl:if>
</xsl:for-each>



#ifdef MAKE_COMPONENT_DLL
#ifdef WIN32

extern "C"
{
__declspec(dllexport) Component *getComponent();
__declspec(dllexport) void releaseComponent(Component *pcom);
}

Component *getComponent()
{
	return new <xsl:value-of select="name"/>();
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
	return new <xsl:value-of select="name"/>();
}

void releaseComponent(Component *com)
{
	delete com;
}
#endif
#endif

</xsl:template>
<!--
** Copyright (C) 2008 ETRI, All rights reserved
**  
**  
** This programs is the production of ETRI research & development activity;
** you cannot use it and cannot redistribute it and cannot modify it and
** cannnot  store it in any media(disk, photo or otherwise) without the prior
** permission of ETRI.
** 
** You should have received the license for this program to use any purpose.
** If not, contact the Electronics and Telecommunications Research Institute
** [ETRI], DaeDog DanJi, TaeJeon, 305-350, Korea.
** 
** NO PART OF THIS WORK BY THE THIS COPYRIGHT HEREON MAY BE REPRODUCED, STORED
** IN RETRIEVAL SYSTENS, IN ANY FORM OR BY ANY MEANS, ELECTRONIC, MECHANICAL,
** PHOTOCOPYING, RECORDING OR OTHERWISE, WITHOUT THE PRIOR PERMISSION OF ETRI
**
** @Author: sby (sby@etri.re.kr)
-->

<!-- xsl:template name="dataPortHeader" match="struct"-->
<xsl:template name="dataPortHeader">
#ifndef _<xsl:value-of select="@name"/>_DATATYPE_H
#define _<xsl:value-of select="@name"/>_DATATYPE_H
#include &lt;boost/shared_ptr.hpp&gt;
#include &lt;boost/serialization/split_member.hpp&gt;
#include &lt;boost/serialization/shared_ptr.hpp&gt;

/*
** If you want to serialize binary, unmark following statements
** typedef boost::shared_ptr&lt;char&gt; BytePtr;
*/
class <xsl:value-of select="@name"/>
{
protected:
	friend class boost::serialization::access;
	<xsl:for-each select="item"><xsl:value-of select="@type"/><xsl:text> </xsl:text>m_<xsl:value-of select="current()"/>;
	</xsl:for-each>
	/** for using binary... 
	BytePtr m_image;
	**/

	template &lt;class Archive&gt;
	void save(Archive &amp;ar, const unsigned int) const
	{
		<xsl:for-each select="item">ar &amp; m_<xsl:value-of select="current()"/>;
		</xsl:for-each>

		/* To serialize binary 
		** m_size is size of binary data to be saved
		ar.save_binary( m_image.get(), m_size);
		*/
	}

	template &lt;class Archive&gt;
	void load(Archive &amp;ar, const unsigned int)
	{
		<xsl:for-each select="item">ar &amp; m_<xsl:value-of select="current()"/>;
		</xsl:for-each>

		/* To serialize binary 
		** m_size is size of binary data to be saved
		m_image.reset(new char [m_size]);
		char *img = m_image.get();
		ar.load_binary( img, m_size);
		**/
	}
	BOOST_SERIALIZATION_SPLIT_MEMBER()
public:
	<xsl:value-of select="@name"/>()
	{
	}

	<xsl:value-of select="@name"/>(<xsl:for-each select="item"><xsl:if test="position()!=1">,</xsl:if><xsl:value-of select="@type"/>&amp;<xsl:text> </xsl:text>in_<xsl:value-of select="current()"/></xsl:for-each>)
	{
		<xsl:for-each select="item">m_<xsl:value-of select="current()"/> = in_<xsl:value-of select="current()"/>;
		</xsl:for-each>
	}

	<xsl:for-each select="item">
	<xsl:value-of select="@type"/>&amp;<xsl:text> </xsl:text>get<xsl:value-of select="translate(substring(current(),1,1), 'abcedefghijklmnopqrstuvwxyz','ABCEDEFGHIJKLMNOPQRSTUVWXYZ')"/><xsl:value-of select="substring(current(),2)"/>()
	{
		return m_<xsl:value-of select="current()"/>;
	}

	void set<xsl:value-of select="translate(substring(current(),1,1), 'abcedefghijklmnopqrstuvwxyz','ABCEDEFGHIJKLMNOPQRSTUVWXYZ')"/><xsl:value-of select="substring(current(),2)"/>(<xsl:value-of select="@type"/>&amp;<xsl:text> </xsl:text>in_<xsl:value-of select="current()"/>)
	{
		m_<xsl:value-of select="current()"/> = in_<xsl:value-of select="current()"/>;
	}

	</xsl:for-each>

};
#endif
</xsl:template>
<!--
** Copyright (C) 2008 ETRI, All rights reserved
**  
**  
** This programs is the production of ETRI research & development activity;
** you cannot use it and cannot redistribute it and cannot modify it and
** cannnot  store it in any media(disk, photo or otherwise) without the prior
** permission of ETRI.
** 
** You should have received the license for this program to use any purpose.
** If not, contact the Electronics and Telecommunications Research Institute
** [ETRI], DaeDog DanJi, TaeJeon, 305-350, Korea.
** 
** NO PART OF THIS WORK BY THE THIS COPYRIGHT HEREON MAY BE REPRODUCED, STORED
** IN RETRIEVAL SYSTENS, IN ANY FORM OR BY ANY MEANS, ELECTRONIC, MECHANICAL,
** PHOTOCOPYING, RECORDING OR OTHERWISE, WITHOUT THE PRIOR PERMISSION OF ETRI
**
** @Author: sby (sby@etri.re.kr)
-->

<!-- xsl:template name="methodPortHeader" match="service_port_type_profile" -->
/*
 *  Generated sources by OPRoS Component Generator (OCG)
 *  
 */
<!-- xsl:apply-templates select="service_port_type"/ -->
<!--/xsl:template -->
<xsl:template name="methodPortHeader"> <!-- //match="service_port_type" -->
<xsl:param name="portType"/>
<xsl:choose>
	<xsl:when test="$portType='provided'">
		<xsl:call-template name="ProvidedTemplate"/>
	</xsl:when>
	<xsl:otherwise>
		<xsl:call-template name="RequiredTemplate"/>
	</xsl:otherwise>
</xsl:choose>
</xsl:template>

<xsl:template name="RequiredTemplate">
<xsl:variable name="className" select="type_name"/>

#ifndef _<xsl:value-of select="$className"/>_REQUIRED_PORT_H
#define _<xsl:value-of select="$className"/>_REQUIRED_PORT_H

#include &lt;OPRoSTypes.h&gt;
#include &lt;InputDataPort.h&gt;
#include &lt;OutputDataPort.h&gt;
#include &lt;InputEventPort.h&gt;
#include &lt;OutputEventPort.h&gt;
#include &lt;ProvidedServicePort.h&gt;
#include &lt;RequiredServicePort.h&gt;
#include &lt;ProvidedMethod.h&gt;
#include &lt;RequiredMethod.h&gt;

<xsl:if test="method/param/type">
<xsl:for-each select="method/param">
	<xsl:variable name="m_name" select="../@name"/>
	<xsl:variable name="t_name" select="name"/>
	<xsl:variable name="t_type" select="type"/>
	<xsl:choose>
	<xsl:when test="count(//method/param/type[.=$t_type])=1">
		<xsl:choose>
		<xsl:when test="starts-with($t_type,'std::')">
		<xsl:choose>
		<xsl:when test="contains($t_type,'&lt;')">
#include &lt;<xsl:value-of select="normalize-space(substring-before(substring-after($t_type,'std::'),'&lt;'))"/>&gt;
		</xsl:when>
		<xsl:otherwise>
#include &lt;<xsl:value-of select="normalize-space(substring-after($t_type,'std::'))"/>&gt;
		</xsl:otherwise>
		</xsl:choose>
		</xsl:when>
		<xsl:when test="$t_type='int'"/>
		<xsl:when test="$t_type='short'"/>
		<xsl:when test="$t_type='long'"/>
		<xsl:when test="$t_type='char'"/>
		<xsl:when test="contains($t_type,'unsigned')"/>
		<xsl:when test="$t_type='bool'"/>
		<xsl:when test="$t_type='float'"/>
		<xsl:when test="$t_type='double'"/>
		<xsl:when test="$t_type='Property'"/>
		<xsl:otherwise>
			<xsl:choose>
			<xsl:when test="contains(normalize-space($t_type),' *')">
			<xsl:if test="not(substring-before(normalize-space($t_type),' *')='Property')">
#include "<xsl:value-of select="substring-before(normalize-space($t_type),' *')"/>.h"
			</xsl:if>
			</xsl:when>
			<xsl:when test="contains(normalize-space($t_type),'*')">
			<xsl:if test="not(substring-before(normalize-space($t_type),'*')='Property')">
#include "<xsl:value-of select="substring-before(normalize-space($t_type),'*')"/>.h"
			</xsl:if>	
			</xsl:when>
			<xsl:otherwise>
#include "<xsl:value-of select="$t_type"/>.h"
			</xsl:otherwise>
			</xsl:choose>
		</xsl:otherwise>
		</xsl:choose>
	</xsl:when>
	<xsl:otherwise>
	<xsl:variable name="nv" select="//method/param[type=$t_type]/name"/>
	<xsl:if test="contains($nv,$t_name)">
		<xsl:choose>
		<xsl:when test="starts-with($t_type,'std::')">
		<xsl:choose>
		<xsl:when test="contains($t_type,'&lt;')">
#include &lt;<xsl:value-of select="normalize-space(substring-before(substring-after($t_type,'std::'),'&lt;'))"/>&gt;
		</xsl:when>
		<xsl:otherwise>
#include &lt;<xsl:value-of select="normalize-space(substring-after($t_type,'std::'))"/>&gt;
		</xsl:otherwise>
		</xsl:choose>
		</xsl:when>
		<xsl:when test="$t_type='int'"/>
		<xsl:when test="$t_type='short'"/>
		<xsl:when test="$t_type='long'"/>
		<xsl:when test="$t_type='char'"/>
		<xsl:when test="contains($t_type,'unsigned')"/>
		<xsl:when test="$t_type='bool'"/>
		<xsl:when test="$t_type='float'"/>
		<xsl:when test="$t_type='double'"/>
		<xsl:when test="$t_type='Property'"/>
		<xsl:otherwise>
			<xsl:choose>
			<xsl:when test="contains(normalize-space($t_type),' *')">
			<xsl:if test="not(substring-before(normalize-space($t_type),' *')='Property')">
#include "<xsl:value-of select="substring-before(normalize-space($t_type),' *')"/>.h"
			</xsl:if>
			</xsl:when>
			<xsl:when test="contains(normalize-space($t_type),'*')">
			<xsl:if test="not(substring-before(normalize-space($t_type),'*')='Property')">
#include "<xsl:value-of select="substring-before(normalize-space($t_type),'*')"/>.h"
			</xsl:if>	
			</xsl:when>
			<xsl:otherwise>
#include "<xsl:value-of select="$t_type"/>.h"
			</xsl:otherwise>
			</xsl:choose>
		</xsl:otherwise>
		</xsl:choose>
	</xsl:if>
	</xsl:otherwise>
	</xsl:choose>
</xsl:for-each>
</xsl:if>



/*
 * 
 * <xsl:value-of select="$className"/> : public RequiredServicePort
 *
 */
class <xsl:value-of select="$className"/>Required
   :public RequiredServicePort
{
protected:
<xsl:for-each select="method">
	typedef RequiredMethod&lt;<xsl:value-of select="@return_type"/>&gt; <xsl:value-of select="@name"/>FuncType;
	<xsl:value-of select="@name"/>FuncType *<xsl:value-of select="@name"/>Func;
</xsl:for-each>
public:
	//
	// Constructor
	//
	<xsl:value-of select="$className"/>Required()
	{
            <xsl:for-each select="method"> <xsl:value-of select="@name"/>Func = NULL;
            </xsl:for-each>
	   setup();
	}

	// method implementation for required method
	<xsl:for-each select="method">
        <xsl:value-of select="@return_type"/><xsl:text> </xsl:text>
        <xsl:value-of select="@name"/>(<xsl:for-each select="param"><xsl:if test="position()!=1">,</xsl:if><xsl:value-of select="type"/><xsl:text> </xsl:text><xsl:value-of select="name"/></xsl:for-each>)
	{
            assert(<xsl:value-of select="@name"/>Func != NULL);
	<xsl:choose>
		<xsl:when test="@return_type='void'">
            (*<xsl:value-of select="@name"/>Func)(<xsl:for-each select="param"><xsl:if test="position()!=1">,</xsl:if><xsl:value-of select="name"/></xsl:for-each>);
		</xsl:when>
		<xsl:otherwise>
            return (*<xsl:value-of select="@name"/>Func)(<xsl:for-each select="param"><xsl:if test="position()!=1">,</xsl:if><xsl:value-of select="name"/></xsl:for-each>);
		</xsl:otherwise>
	</xsl:choose>
	}

	</xsl:for-each>

    // generated setup function
    virtual void setup()
    {
        Method *ptr_method;
    <xsl:for-each select="method">
        ptr_method = makeRequiredMethod(&amp;<xsl:value-of select="$className"/>Required::<xsl:value-of select="@name"/>,"<xsl:value-of select="@name"/>");
        assert(ptr_method != NULL);
        addMethod("<xsl:value-of select="@name"/>",ptr_method);
        <xsl:value-of select="@name"/>Func = reinterpret_cast&lt;<xsl:value-of select="@name"/>FuncType *&gt;(ptr_method);
        ptr_method = NULL;

    </xsl:for-each>
    }
};
#endif
</xsl:template>

<xsl:template name="ProvidedTemplate">
<xsl:variable name="className" select="type_name"/>
<xsl:variable name="providerName" select="concat('I',type_name)"/>

#ifndef _<xsl:value-of select="$className"/>_PROVIDED_PORT_H
#define _<xsl:value-of select="$className"/>_PROVIDED_PORT_H

#include &lt;OPRoSTypes.h&gt;
#include &lt;InputDataPort.h&gt;
#include &lt;OutputDataPort.h&gt;
#include &lt;InputEventPort.h&gt;
#include &lt;OutputEventPort.h&gt;
#include &lt;ProvidedServicePort.h&gt;
#include &lt;RequiredServicePort.h&gt;
#include &lt;ProvidedMethod.h&gt;
#include &lt;RequiredMethod.h&gt;

<xsl:if test="method/param/type">
<xsl:for-each select="method/param">
	<xsl:variable name="m_name" select="../@name"/>
	<xsl:variable name="t_name" select="name"/>
	<xsl:variable name="t_type" select="type"/>
	<xsl:choose>
	<xsl:when test="count(//method/param/type[.=$t_type])=1">
		<xsl:choose>
		<xsl:when test="starts-with($t_type,'std::')">
		<xsl:choose>
		<xsl:when test="contains($t_type,'&lt;')">
#include &lt;<xsl:value-of select="normalize-space(substring-before(substring-after($t_type,'std::'),'&lt;'))"/>&gt;
		</xsl:when>
		<xsl:otherwise>
#include &lt;<xsl:value-of select="normalize-space(substring-after($t_type,'std::'))"/>&gt;
		</xsl:otherwise>
		</xsl:choose>
		</xsl:when>
		<xsl:when test="$t_type='int'"/>
		<xsl:when test="$t_type='short'"/>
		<xsl:when test="$t_type='long'"/>
		<xsl:when test="$t_type='char'"/>
		<xsl:when test="contains($t_type,'unsigned')"/>
		<xsl:when test="$t_type='bool'"/>
		<xsl:when test="$t_type='float'"/>
		<xsl:when test="$t_type='double'"/>
		<xsl:when test="$t_type='Property'"/>
		<xsl:otherwise>
			<xsl:choose>
			<xsl:when test="contains(normalize-space($t_type),' *')">
			<xsl:if test="not(substring-before(normalize-space($t_type),' *')='Property')">
#include "<xsl:value-of select="substring-before(normalize-space($t_type),' *')"/>.h"
			</xsl:if>
			</xsl:when>
			<xsl:when test="contains(normalize-space($t_type),'*')">
			<xsl:if test="not(substring-before(normalize-space($t_type),'*')='Property')">
#include "<xsl:value-of select="substring-before(normalize-space($t_type),'*')"/>.h"
			</xsl:if>	
			</xsl:when>
			<xsl:otherwise>
#include "<xsl:value-of select="$t_type"/>.h"
			</xsl:otherwise>
			</xsl:choose>
		</xsl:otherwise>
		</xsl:choose>
	</xsl:when>
	<xsl:otherwise>
	<xsl:variable name="nv" select="//method/param[type=$t_type]/name"/>
	<xsl:if test="contains($nv,$t_name)">
		<xsl:choose>
		<xsl:when test="starts-with($t_type,'std::')">
		<xsl:choose>
		<xsl:when test="contains($t_type,'&lt;')">
#include &lt;<xsl:value-of select="normalize-space(substring-before(substring-after($t_type,'std::'),'&lt;'))"/>&gt;
		</xsl:when>
		<xsl:otherwise>
#include &lt;<xsl:value-of select="normalize-space(substring-after($t_type,'std::'))"/>&gt;
		</xsl:otherwise>
		</xsl:choose>
		</xsl:when>
		<xsl:when test="$t_type='int'"/>
		<xsl:when test="$t_type='short'"/>
		<xsl:when test="$t_type='long'"/>
		<xsl:when test="$t_type='char'"/>
		<xsl:when test="contains($t_type,'unsigned')"/>
		<xsl:when test="$t_type='bool'"/>
		<xsl:when test="$t_type='float'"/>
		<xsl:when test="$t_type='double'"/>
		<xsl:when test="$t_type='Property'"/>
		<xsl:otherwise>
			<xsl:choose>
			<xsl:when test="contains(normalize-space($t_type),' *')">
			<xsl:if test="not(substring-before(normalize-space($t_type),' *')='Property')">
#include "<xsl:value-of select="substring-before(normalize-space($t_type),' *')"/>.h"
			</xsl:if>
			</xsl:when>
			<xsl:when test="contains(normalize-space($t_type),'*')">
			<xsl:if test="not(substring-before(normalize-space($t_type),'*')='Property')">
#include "<xsl:value-of select="substring-before(normalize-space($t_type),'*')"/>.h"
			</xsl:if>	
			</xsl:when>
			<xsl:otherwise>
#include "<xsl:value-of select="$t_type"/>.h"
			</xsl:otherwise>
			</xsl:choose>
		</xsl:otherwise>
		</xsl:choose>
	</xsl:if>
	</xsl:otherwise>
	</xsl:choose>
</xsl:for-each>
</xsl:if>

/*
 * <xsl:value-of select="$providerName"/>
 *
 * The comonent inherits this class and implements methods. 
*/
class <xsl:value-of select="$providerName"/>
{
 public:<xsl:for-each select="method">
    virtual <xsl:value-of select="@return_type"/><xsl:text> </xsl:text><xsl:value-of select="@name"/>(<xsl:for-each select="param"><xsl:if test="position()!=1">,</xsl:if><xsl:value-of select="type"/><xsl:text> </xsl:text><xsl:value-of select="name"/></xsl:for-each>)=0;</xsl:for-each>
 };

/*
 * 
 * <xsl:value-of select="$className"/> : public ProvidedServicePort
 *
 */
class <xsl:value-of select="$className"/>Provided
	:public ProvidedServicePort, public <xsl:value-of select="$providerName"/>
{
protected:
    <xsl:value-of select="$providerName"/> *pcom;

<xsl:for-each select="method">
   typedef ProvidedMethod&lt;<xsl:value-of select="@return_type"/>&gt; <xsl:value-of select="@name"/>FuncType;
   <xsl:value-of select="@name"/>FuncType *<xsl:value-of select="@name"/>Func;
</xsl:for-each>

public: // default method<xsl:for-each select="method">
   virtual <xsl:value-of select="@return_type"/><xsl:text> </xsl:text><xsl:value-of select="@name"/>(<xsl:for-each select="param"><xsl:if test="position()!=1">,</xsl:if><xsl:value-of select="type"/><xsl:text> </xsl:text><xsl:value-of select="name"/></xsl:for-each>)
   {
		assert(<xsl:value-of select="@name"/>Func != NULL);
		<xsl:choose>
			<xsl:when test="@return_type='void'">(*<xsl:value-of select="@name"/>Func)(<xsl:for-each select="param"><xsl:if test="position()!=1">,</xsl:if><xsl:value-of select="name"/></xsl:for-each>);</xsl:when>
		<xsl:otherwise>
            return (*<xsl:value-of select="@name"/>Func)(<xsl:for-each select="param"><xsl:if test="position()!=1">,</xsl:if><xsl:value-of select="name"/></xsl:for-each>);
		</xsl:otherwise>
	</xsl:choose>
   }</xsl:for-each>


public:
    //
    // Constructor
    //
    <xsl:value-of select="$className"/>Provided(<xsl:value-of select="$providerName"/> *fn)
    {
        pcom = fn;

        <xsl:for-each select="method"> <xsl:value-of select="@name"/>Func = NULL;
        </xsl:for-each>

        setup();
    }

    // generated setup function
    virtual void setup()
    {
        Method *ptr_method;
    <xsl:for-each select="method">
        ptr_method = makeProvidedMethod(&amp;<xsl:value-of select="$providerName"/>::<xsl:value-of select="@name"/>,pcom,"<xsl:value-of select="@name"/>");

        assert(ptr_method != NULL);
        addMethod("<xsl:value-of select="@name"/>",ptr_method);
        <xsl:value-of select="@name"/>Func = reinterpret_cast&lt;<xsl:value-of select="@name"/>FuncType *&gt;(ptr_method);
        ptr_method = NULL;

    </xsl:for-each>
    }
};
#endif
</xsl:template>
<!--
** Copyright (C) 2008 ETRI, All rights reserved
**  
**  
** This programs is the production of ETRI research & development activity;
** you cannot use it and cannot redistribute it and cannot modify it and
** cannnot  store it in any media(disk, photo or otherwise) without the prior
** permission of ETRI.
** 
** You should have received the license for this program to use any purpose.
** If not, contact the Electronics and Telecommunications Research Institute
** [ETRI], DaeDog DanJi, TaeJeon, 305-350, Korea.
** 
** NO PART OF THIS WORK BY THE THIS COPYRIGHT HEREON MAY BE REPRODUCED, STORED
** IN RETRIEVAL SYSTENS, IN ANY FORM OR BY ANY MEANS, ELECTRONIC, MECHANICAL,
** PHOTOCOPYING, RECORDING OR OTHERWISE, WITHOUT THE PRIOR PERMISSION OF ETRI
**
** @Author: sby (sby@etri.re.kr)
-->
<xsl:template name="makeGenerator">
<xsl:param name="name"/>
<xsl:variable name="cfgmodule" select="document('libconfig.cfg')"/>
	<xsl:document href="Makefile.vc" method="text">
		<xsl:call-template name="makeGeneratorVS2008">
			<xsl:with-param name="name" select="$name"/>
			<xsl:with-param name="cfg" select="$cfgmodule"/>
		</xsl:call-template>
	</xsl:document>
	<xsl:document href="Makefile.gcc" method="text">
		<xsl:call-template name="makeGeneratorGCC">
			<xsl:with-param name="name" select="$name"/>
			<xsl:with-param name="cfg" select="$cfgmodule"/>
		</xsl:call-template>
	</xsl:document>
</xsl:template>
<xsl:template name="makeGeneratorVS2008">
<xsl:param name="name"/>
<xsl:param name="cfg"/>
OUTDIR=.\Release
INTDIR=.\Release
OutDir=.\Release
<xsl:choose>
<xsl:when test="$cfg/libconfig">
  <xsl:choose>
	  <xsl:when test="$cfg/libconfig/boost_inc">
BOOST_INC=<xsl:value-of select="$cfg/libconfig/boost_inc"/>
	</xsl:when>
	<xsl:otherwise>
BOOST_INC=./boost/include/boost-1_35 # change this with your boost include path
	</xsl:otherwise>
  </xsl:choose>
  <xsl:choose>
	  <xsl:when test="$cfg/libconfig/boost_lib">
BOOST_LIB=<xsl:value-of select="$cfg/libconfig/boost_lib"/>
	</xsl:when>
	<xsl:otherwise>
BOOST_LIB=./boost/lib                # change this with your boost lib path
	</xsl:otherwise>
  </xsl:choose>
  <xsl:choose>
	  <xsl:when test="$cfg/libconfig/opros_inc">
OPROS_INC=<xsl:value-of select="$cfg/libconfig/opros_inc"/>
	</xsl:when>
	<xsl:otherwise>
OPROS_INC=./OPRoSLib/include         # change this with your opros include path
	</xsl:otherwise>
  </xsl:choose>
  <xsl:choose>
	  <xsl:when test="$cfg/libconfig/opros_lib">
OPROS_LIB=<xsl:value-of select="$cfg/libconfig/opros_lib"/>
	</xsl:when>
	<xsl:otherwise>
OPROS_LIB=./OPRoSLib/lib             # change this with your opros lib path
	</xsl:otherwise>
  </xsl:choose>
</xsl:when>
<xsl:otherwise>
BOOST_INC=./boost/include/boost-1_35 # change this with your boost include path
BOOST_LIB=./boost/lib                # change this with your boost lib path
OPROS_INC=./OPRoSLib/include         # change this with your opros include path
OPROS_LIB=./OPRoSLib/lib             # change this with your opros lib path
</xsl:otherwise>
</xsl:choose>

PROJECT=<xsl:value-of select="$name"/>
TARGET= $(PROJECT).dll
OBJ1 =   $(PROJECT).obj
#OBJ2 =  some.obj

ALL: $(OUTDIR)\$(TARGET)

CLEAN: 
	-@erase "$(INTDIR)\$(TARGET)"
	-@erase "$(INTDIR)\$(OBJ1)"
	-@erase "$(INTDIR)\$(PROJECT).lib"
#	-@erase "$(INTDIR)\$(OBJ2)"
	
	

$(OUTDIR) :
	if not exist "$(OUTDIR)/$(NULL)" mkdir "$(OUTDIR)"
	
INC = /I "$(BOOST_INC)" /I "$(OPROS_INC)"

CPP=cl.exe

CPP_PROJ=/O2 /Oi $(INC) /GL /D "WIN32" /D "_WINDOWS" /D "NDEBUG" /D "MAKE_COMPONENT_DLL" /D "_USRDLL" /D "_WINDLL" /D "_AFXDLL" /D "_UNICODE" \
 /D "UNICODE" /FD /EHsc /MD /Gy /Fo"$(INTDIR)\\" /Fd"$(INTDIR)\\" /W0 /nologo /c /Zi /TP /errorReport:prompt

.cpp{$(INTDIR)}.obj::
	$(CPP) $(CPP_PROJ) $&lt; 

LINK32=link.exe
LINK32_FLAGS=/OUT:"$(OUTDIR)\$(TARGET)" /INCREMENTAL:NO /NOLOGO /libpath:"$(BOOST_LIB)" /libpath:"$(OPROS_LIB)" \
	/DLL /MANIFEST /DEBUG /SUBSYSTEM:WINDOWS /OPT:REF /OPT:ICF /LTCG /DYNAMICBASE /NXCOMPAT /MACHINE:X86 /ERRORREPORT:PROMPT \
	OPRoSLib.lib 


LINK32_OBJS= \
	"$(INTDIR)\$(OBJ1)" 
#	"$(INTDIR)\$(OBJ2)"
	
	
$(OUTDIR)\$(TARGET) : $(OUTDIR) $(DEF_FILE) $(LINK32_OBJS)
	$(LINK32) $(LINK32_FLAGS) $(LINK32_OBJS)	


</xsl:template>

<xsl:template name="makeGeneratorGCC">
<xsl:param name="name"/>
<xsl:param name="cfg"/>
OUTDIR=.\Release
INTDIR=.\Release
OutDir=.\Release
<xsl:choose>
<xsl:when test="$cfg/libconfig">
  <xsl:choose>
	  <xsl:when test="$cfg/libconfig/boost_inc">
BOOST_INC=<xsl:value-of select="$cfg/libconfig/boost_inc"/>
	</xsl:when>
	<xsl:otherwise>
BOOST_INC=./boost/include/boost-1_35 # change this with your boost include path
	</xsl:otherwise>
  </xsl:choose>
  <xsl:choose>
	  <xsl:when test="$cfg/libconfig/boost_lib">
BOOST_LIB=<xsl:value-of select="$cfg/libconfig/boost_lib"/>
	</xsl:when>
	<xsl:otherwise>
BOOST_LIB=./boost/lib                # change this with your boost lib path
	</xsl:otherwise>
  </xsl:choose>
  <xsl:choose>
	  <xsl:when test="$cfg/libconfig/opros_inc">
OPROS_INC=<xsl:value-of select="$cfg/libconfig/opros_inc"/>
	</xsl:when>
	<xsl:otherwise>
OPROS_INC=./OPRoSLib/include         # change this with your opros include path
	</xsl:otherwise>
  </xsl:choose>
  <xsl:choose>
	  <xsl:when test="$cfg/libconfig/opros_lib">
OPROS_LIB=<xsl:value-of select="$cfg/libconfig/opros_lib"/>
	</xsl:when>
	<xsl:otherwise>
OPROS_LIB=./OPRoSLib/lib             # change this with your opros lib path
	</xsl:otherwise>
  </xsl:choose>
  <xsl:choose>
	  <xsl:when test="$cfg/libconfig/stl_inc">
STL_INC=<xsl:value-of select="$cfg/libconfig/stl_inc"/>
	</xsl:when>
	<xsl:otherwise>
STL_INC = ./STL/stlport              # change this with your STL include path
	</xsl:otherwise>
  </xsl:choose>
  <xsl:choose>
	  <xsl:when test="$cfg/libconfig/stl_lib">
STL_LIB=<xsl:value-of select="$cfg/libconfig/stl_lib"/>
	</xsl:when>
	<xsl:otherwise>
STL_LIB = ./STL/lib       	     # change this with your STL lib path
	</xsl:otherwise>
  </xsl:choose>
</xsl:when>
<xsl:otherwise>
BOOST_INC=./boost/include/boost-1_35 # change this with your boost include path
BOOST_LIB=./boost/lib                # change this with your boost lib path
OPROS_INC=./OPRoSLib/include         # change this with your opros include path
OPROS_LIB=./OPRoSLib/lib             # change this with your opros lib path
STL_INC = ./STL/stlport              # change this with your STL include path
STL_LIB = ./STL/lib       	     # change this with your STL lib path
</xsl:otherwise>
</xsl:choose>
SO_TYPE	= so                         # shared object extensions (so or dll)

PROJECT = <xsl:value-of select="$name"/>

INC = -I$(STL_INC) -I$(BOOST_INC) -I$(OPROS_INC)

CXXFLAGS = -O2 -Wall -fmessage-length=0 -mthreads -fPIC -DMAKE_COMPONENT_DLL $(INC)

LFLAGS = -L$(BOOST_LIB) -L$(STL_LIB) -L$(OPROS_LIB)

OBJS1 = $(PROJECT).o

LIBS = -lOPRoSV2Lib -lstlport.5.1 -lboost_iostreams-mgw34-mt-p -lboost_serialization-mgw34-mt-p

TARGET = $(PROJECT).$(SO_TYPE)

$(TARGET): $(OBJS1)
	$(CXX) $(LFLAGS) -shared -Wl,-soname,$@ -o $@ $(OBJS1) $(LIBS)

all: $(TARGET)

default: $(TARGET)

clean:
	del -f $(OBJS1) $(TARGET)
</xsl:template>
<!--
** Copyright (C) 2008 ETRI, All rights reserved
**  
**  
** This programs is the production of ETRI research & development activity;
** you cannot use it and cannot redistribute it and cannot modify it and
** cannnot  store it in any media(disk, photo or otherwise) without the prior
** permission of ETRI.
** 
** You should have received the license for this program to use any purpose.
** If not, contact the Electronics and Telecommunications Research Institute
** [ETRI], DaeDog DanJi, TaeJeon, 305-350, Korea.
** 
** NO PART OF THIS WORK BY THE THIS COPYRIGHT HEREON MAY BE REPRODUCED, STORED
** IN RETRIEVAL SYSTENS, IN ANY FORM OR BY ANY MEANS, ELECTRONIC, MECHANICAL,
** PHOTOCOPYING, RECORDING OR OTHERWISE, WITHOUT THE PRIOR PERMISSION OF ETRI
**
** @Author: sby (sby@etri.re.kr)
-->

<xsl:template match="/">
	<xsl:variable name="module" select="document(concat($filename,'.xml'))"/>
	<xsl:apply-templates select="$module/*"/>
</xsl:template>
<xsl:template match="component">
<!--
** Modify (C) 2008 ED Corp.
** @Author: sevensky(Juwon Kim) (jwkim@ed.co.kr)
-->
	<xsl:document href="{concat($outpath,concat(name,'.h'))}" method="text">
<!--  -->	
	<xsl:call-template name="componentHeader">
		<xsl:with-param name="filename" select="name"/>
	</xsl:call-template>
	</xsl:document>
<!--
** Modify (C) 2008 ED Corp.
** @Author: sevensky(Juwon Kim) (jwkim@ed.co.kr)
-->
	<xsl:document href="{concat($outpath,concat(name,'.cpp'))}" method="text">
<!--  -->	
	<xsl:call-template name="componentSource">
		<xsl:with-param name="filename" select="name"/>
	</xsl:call-template>
	</xsl:document>
</xsl:template>
<xsl:template match="component_profile">
<!--
** Modify (C) 2008 ED Corp.
** @Author: sevensky(Juwon Kim) (jwkim@ed.co.kr)
-->
	<xsl:document href="{concat($outpath,concat(name,'.h'))}" method="text">
<!--  -->	
	<xsl:call-template name="componentHeader">
		<xsl:with-param name="filename" select="name"/>
	</xsl:call-template>
	</xsl:document>
<!--
** Modify (C) 2008 ED Corp.
** @Author: sevensky(Juwon Kim) (jwkim@ed.co.kr)
-->
	<xsl:document href="{concat($outpath,concat(name,'.cpp'))}" method="text">
<!--  -->	
	<xsl:call-template name="componentSource">
		<xsl:with-param name="filename" select="name"/>
	</xsl:call-template>
	</xsl:document>
	<xsl:call-template name="makeGenerator">
  		<xsl:with-param name="name" select="name" /> 
  	</xsl:call-template>
</xsl:template>

<xsl:template match="service_port_type">
	<xsl:choose>
	<xsl:when test="$usage='none'">
<!--
** Modify (C) 2008 ED Corp.
** @Author: sevensky(Juwon Kim) (jwkim@ed.co.kr)
-->	
		<xsl:document href="{concat($outpath,concat(type_name,'Provided.h'))}" method="text">
<!-- -->		
			<xsl:call-template name="methodPortHeader">
				<xsl:with-param name="portType" select="'provided'"/>
			</xsl:call-template>
		</xsl:document>
<!--
** Modify (C) 2008 ED Corp.
** @Author: sevensky(Juwon Kim) (jwkim@ed.co.kr)
-->		
		<xsl:document href="{concat($outpath,concat(type_name,'Required.h'))}" method="text">
<!--  -->		
			<xsl:call-template name="methodPortHeader">
				<xsl:with-param name="portType" select="'required'"/>
			</xsl:call-template>
		</xsl:document>
	</xsl:when>
	<xsl:when test="$usage='provided'">
<!--
** Modify (C) 2008 ED Corp.
** @Author: sevensky(Juwon Kim) (jwkim@ed.co.kr)
-->
		<xsl:document href="{concat($outpath,concat(type_name,'Provided.h'))}" method="text">
<!--  -->
			<xsl:call-template name="methodPortHeader">
				<xsl:with-param name="portType" select="'provided'"/>
			</xsl:call-template>
		</xsl:document>
	</xsl:when>
	<xsl:when test="$usage='required'">
<!--
** Modify (C) 2008 ED Corp.
** @Author: sevensky(Juwon Kim) (jwkim@ed.co.kr)
-->
		<xsl:document href="{concat($outpath,concat(type_name,'Required.h'))}" method="text">
<!--  -->		
			<xsl:call-template name="methodPortHeader">
				<xsl:with-param name="portType" select="'required'"/>
			</xsl:call-template>
		</xsl:document>
	</xsl:when>
	<xsl:otherwise>
<!--
** Modify (C) 2008 ED Corp.
** @Author: sevensky(Juwon Kim) (jwkim@ed.co.kr)
-->
		<xsl:document href="{concat($outpath,concat(type_name,'Provided.h'))}" method="text">
			<xsl:call-template name="methodPortHeader">
				<xsl:with-param name="portType" select="'provided'"/>
			</xsl:call-template>
		</xsl:document>
		<xsl:document href="{concat($outpath,concat(type_name,'Required.h'))}" method="text">
			<xsl:call-template name="methodPortHeader">
				<xsl:with-param name="portType" select="'required'"/>
			</xsl:call-template>
		</xsl:document>
<!-- -->
	</xsl:otherwise>
	</xsl:choose>
</xsl:template>
<xsl:template match="data_type">
<xsl:for-each select="struct">
<!--
** Modify (C) 2008 ED Corp.
** @Author: sevensky(Juwon Kim) (jwkim@ed.co.kr)
-->
	<xsl:document href="{concat($outpath,concat(@name,'.h'))}" method="text">
<!--  -->
		<xsl:call-template name="dataPortHeader"/>
	</xsl:document>
</xsl:for-each>
</xsl:template>
</xsl:stylesheet>
