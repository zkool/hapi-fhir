package ca.uhn.fhir.tinder.model;

import static org.apache.commons.lang.StringUtils.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public abstract class BaseElement {

	private String myBinding;
	private String myBindingClass;
	private String myCardMax;
	private String myCardMin;
	private Map<String, Slicing> myChildElementNameToSlicing = new HashMap<String, Slicing>();
	private List<BaseElement> myChildren;
	private String myComments;
	private String myDefinition;
	private String myElementName;
	private String myElementParentName;
	private String myName;
	private String myRequirement;
	private boolean myResourceRef = false;
	private String myShortName;
	private List<String> myType;
	private String myV2Mapping;
	private String myExtensionUrl;

	public void addChild(Child theElem) {
		if (myChildren == null) {
			myChildren = new ArrayList<BaseElement>();
		}
		myChildren.add(theElem);
	}

	public String getBinding() {
		return myBinding;
	}

	public String getBindingClass() {
		return myBindingClass;
	}

	public String getCardMax() {
		return myCardMax;
	}

	public String getCardMin() {
		return myCardMin;
	}

	public Map<String, Slicing> getChildElementNameToSlicing() {
		return myChildElementNameToSlicing;
	}

	public List<BaseElement> getChildren() {
		if (myChildren == null) {
			myChildren = new ArrayList<BaseElement>();
		}
		return myChildren;
	}

	public String getComments() {
		return myComments;
	}

	public String getDefinition() {
		return toStringConstant(myDefinition);
	}

	private String toStringConstant(String theDefinition) {
		if (theDefinition==null) {
			return "";
		}
		StringBuffer b = new StringBuffer();
		for (char next : theDefinition.toCharArray()) {
			if (next < ' ') {
				continue;
			}
			if (next == '"') {
				b.append('\\');
			}
			b.append(next);
		}
		return b.toString().trim();
	}

	public String getElementName() {
		return myElementName;
	}

	public String getElementParentName() {
		return myElementParentName;
	}

	public String getName() {
		return myName;
	}

	public String getRequirement() {
		return myRequirement;
	}

	public List<ResourceBlock> getResourceBlockChildren() {
		ArrayList<ResourceBlock> retVal = new ArrayList<ResourceBlock>();
		for (BaseElement next : getChildren()) {
			if (next instanceof ResourceBlock) {
				retVal.add((ResourceBlock) next);
			}
		}
		return retVal;
	}

	public String getShortName() {
		return toStringConstant(myShortName);
	}

	public List<String> getType() {
		if (myType == null) {
			myType = new ArrayList<String>();
		}
		return myType;
	}

	public abstract String getTypeSuffix();

	public String getV2Mapping() {
		return myV2Mapping;
	}

	public boolean isHasMultipleTypes() {
		return getType().size() > 1;
	}

	public boolean isResourceRef() {
		return myResourceRef;
	}

	public void setBinding(String theCellValue) {
		myBinding = theCellValue;
	}

	public void setBindingClass(String theBindingClass) {
		myBindingClass = theBindingClass;
	}

	public void setCardMax(String theCardMax) {
		myCardMax = theCardMax;
	}

	public void setCardMin(String theCardMin) {
		myCardMin = theCardMin;
	}

	public void setChildElementNameToSlicing(Map<String, Slicing> theChildElementNameToSlicing) {
		myChildElementNameToSlicing = theChildElementNameToSlicing;
	}

	public void setComments(String theComments) {
		myComments = theComments;
	}

	public void setDefinition(String theDefinition) {
		myDefinition = theDefinition;
	}

	public void setElementName(String theName) {
		myElementName = theName;
	}
	
	public void setElementNameAndDeriveParentElementName(String theName) {
		int lastDot = theName.lastIndexOf('.');
		if (lastDot == -1) {
			myElementName = (theName);
		} else {
			String elementName = theName.substring(lastDot + 1);
			String elementParentName = theName.substring(0, lastDot);
			myElementName = (elementName);
			myElementParentName = (elementParentName);
		}
	}

	public void setName(String theName) {
		myName = theName;
	}

	public void setRequirement(String theString) {
		myRequirement = theString;
	}

	public void setResourceRef(boolean theResourceRef) {
		myResourceRef = theResourceRef;
	}

	public void setShortName(String theShortName) {
		myShortName = theShortName;
	}

	public void setTypeFromString(String theType) {
		if (theType == null) {
			myType = null;
			return;
		}
		String typeString = theType;
		if (typeString.toLowerCase().startsWith("resource(")) {
			typeString = typeString.substring("Resource(".length(), typeString.length() - 1);
			myResourceRef = true;
		} else if (typeString.startsWith("@")) {
			typeString = typeString.substring(1);
			typeString = ResourceBlock.convertFhirPathNameToClassName(typeString);
		} else if (typeString.equals("*")) {
			typeString = "IDatatype";
		}

		if (StringUtils.isNotBlank(typeString)) {
			String[] types = typeString.replace("=", "").split("\\|");
			for (String nextType : types) {
				nextType = nextType.trim();
				if (nextType.toLowerCase().startsWith("resource(")) {
					nextType = nextType.substring("Resource(".length(), nextType.length() - 1);
					nextType = nextType.substring(0, 1).toUpperCase() + nextType.substring(1);
				} else {
					nextType = nextType.substring(0, 1).toUpperCase() + nextType.substring(1);
					nextType = nextType + getTypeSuffix();
				}

				if (isNotBlank(nextType)) {
					getType().add(nextType);
				}
			}
		}

	}

	public void setV2Mapping(String theV2Mapping) {
		myV2Mapping = theV2Mapping;
	}

	public void setExtensionUrl(String theExtensionUrl) {
		myExtensionUrl = theExtensionUrl;
	}

	public boolean isHasExtensionUrl() {
		return StringUtils.isNotBlank(myExtensionUrl);
	}

	public String getExtensionUrl() {
		return myExtensionUrl;
	}

}
