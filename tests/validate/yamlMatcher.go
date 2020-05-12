package validate

import (
	"fmt"
	"io/ioutil"

	. "github.com/onsi/gomega"
	"github.com/onsi/gomega/types"
	"gopkg.in/yaml.v2"
)

func ValidateYamlObject(expected interface{}, failureMessage *string) types.GomegaMatcher {
	return &validateYaml{
		expected: expected,
	}
}

type validateYaml struct {
	expected interface{}
}

func (matcher *validateYaml) Match(actual interface{}) (success bool, err error) {
	switch expectedType := matcher.expected.(type) {
	case map[interface{}]interface{}:
		actualMap, ok := actual.(map[interface{}]interface{})
		if !ok {
			return false, typeMismatchError(actual, expectedType)
		}
		for key, value := range actualMap {
			if expectedTypeValue, ok := expectedType[key.(string)]; ok {
				nestedExpectedObject := validateYaml{expectedTypeValue}
				_, err := nestedExpectedObject.Match(actualMap[key.(string)])
				if err != nil {
					return false, recursiveCallError(nestedExpectedObject, actualMap[key.(string)], err)
				}
			} else {
				return false, valueComparisonError(actual, value, expectedType, expectedTypeValue)
			}
		}
		return true, nil
	case []interface{}:
		actualSlice, ok := actual.([]interface{})
		if !ok {
			return false, typeMismatchError(actual, expectedType)
		}
		for i, value := range actualSlice {
			if expectedTypeValue := expectedType[i]; ok {
				nestedExpectedObject := validateYaml{expectedTypeValue}
				_, err := nestedExpectedObject.Match(actualSlice[i])
				if err != nil {
					return false, recursiveCallError(nestedExpectedObject, actualSlice[i], err)
				}
			} else {
				return false, valueComparisonError(actual, value, expectedType, expectedTypeValue)
			}
		}
		return true, nil
	case string:
		actualString, ok := actual.(string)
		if !ok {
			return false, typeMismatchError(actual, expectedType)
		}
		if actualString != expectedType {
			return false, valueComparisonError(actualString, nil, expectedType, nil)
		}
		return true, nil
	case int:
		actualInt, ok := actual.(int)
		if !ok {
			return false, typeMismatchError(actual, expectedType)
		}
		if actualInt != expectedType {
			return false, valueComparisonError(actualInt, nil, expectedType, nil)
		}
		return true, nil
	case bool:
		actualBool, ok := actual.(bool)
		if !ok {
			return false, typeMismatchError(actual, expectedType)
		}
		if actualBool != expectedType {
			return false, valueComparisonError(actualBool, nil, expectedType, nil)
		}
		return true, nil
	default:
		return false, fmt.Errorf("expectedType of %T did not match any expected types", expectedType)
	}
}

func (matcher *validateYaml) FailureMessage(actual interface{}) (message string) {
	return fmt.Sprintf("Expected %v to be the same value as %v", actual, matcher.expected)
}

func (matcher *validateYaml) NegatedFailureMessage(actual interface{}) (message string) {
	return fmt.Sprintf("Expected %v to be the same value as %v", actual, matcher.expected)
}

func ExpectYamlToParse(path string) interface{} {
	var output interface{}
	file, err := ioutil.ReadFile(path)
	failMessage := fmt.Sprintf("File at the path, %s, cannot be found. File may be in wrong location or misnamed.\n", path)
	Expect(err).To(BeNil(), failMessage)
	err = yaml.Unmarshal([]byte(file), &output)
	failMessage = fmt.Sprintf("File at the path, %s, could not be parsed as YAML. Error: %s\n", path, err)
	Expect(err).To(BeNil(), failMessage)
	return output
}

func typeMismatchError(actual interface{}, expected interface{}) error {
	return fmt.Errorf("Your value type %T, is not the same as the correct type, %T", expected, actual)
}

func valueComparisonError(actual interface{}, actualValue interface{}, expected interface{}, expectedValue interface{}) error {
	var errStr string
	if actualValue == nil && expectedValue == nil {
		switch actualType := actual.(type) {
		case string:
			errStr = fmt.Sprintf("Your value, %v, did not have the correct value, %v", expected.(string), actualType)
		case int:
			errStr = fmt.Sprintf("Your value, %d, did not have the correct value, %d", expected.(int), actualType)
		case bool:
		default:
			errStr = fmt.Sprintf("Your value, %t, did not have the correct value, %t", expected.(bool), actualType)
		}
	} else {
		errStr = fmt.Sprintf("Your %T with value, %T, did not have the correct value, %T , of field,  %T", actual, actualValue, expected, expectedValue)
	}
	return fmt.Errorf(errStr)
}

func recursiveCallError(expected interface{}, actual interface{}, err error) error {
	return err
}
