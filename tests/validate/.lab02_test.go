package validate

import (
	"fmt"
	"io/ioutil"
	"os"

	"gopkg.in/yaml.v2"

	. "github.com/onsi/ginkgo"
	. "github.com/onsi/gomega"
)

var _ = Describe("Lab 2 ", func() {
	var failMessage string

	BeforeEach(func() {
		failMessage = ""
	})

	Context("Step 3", func() {
		It("should be a valid skaffold configuration", func() {
			var skaffold interface{}
			skaffoldFile, err := ioutil.ReadFile("../skaffold.yaml")
			if err != nil {
				Skip("skaffold.yaml not found")
			}

			err = yaml.Unmarshal(skaffoldFile, &skaffold)
			Expect(err).ToNot(HaveOccurred())
			//failures := InterceptGomegaFailures(func() {

			failMessage = "Incorrect apiVersion in skaffold.yaml\n"
			Expect(treeValue(skaffold, []interface{}{"apiVersion"})).To(Equal("skaffold/v1beta12"), failMessage)
			failMessage = "First build artifact in skaffold.yaml should be \"springtrader\"\n"
			Expect(treeValue(skaffold, []interface{}{"build", "artifacts", 0, "image"})).To(Equal("springtrader"), failMessage)
			failMessage = "Second build artifact in skaffold.yaml should be \"sqlfdb\"\n"
			Expect(treeValue(skaffold, []interface{}{"build", "artifacts", 1, "image"})).To(Equal("sqlfdb"), failMessage)
		})
	})

	AfterEach(func() {
		log.Printf("%v\n", CurrentGinkgoTestDescription())
		if CurrentGinkgoTestDescription().Failed {
			ConcatenatedMessage += failMessage
		}
	})
})
