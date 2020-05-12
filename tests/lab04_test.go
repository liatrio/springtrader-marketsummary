package main

import (
	"log"

	. "github.com/onsi/ginkgo"
	. "github.com/onsi/gomega"
)

var _ = Describe("Lab 4 Deployments", func() {
	var failMessage string

	BeforeEach(func() {
		failMessage = ""
	})

	Context("Step 1.1", func() {
		It("should have a canary.yaml file", func() {
			failMessage = "canary.yaml doesn't exist or is in the wrong location\n"
			Expect("../charts/marketsummary/templates/canary.yaml").To(BeAnExistingFile(), failMessage)
		})
	})

	Context("Step 1.2", func() {
		It("should have a values.yaml file", func() {
			failMessage = "values.yaml doesn't exist or is in the wrong location\n"
			Expect("../charts/marketsummary/values.yaml").To(BeAnExistingFile(), failMessage)
		})
	})

	AfterEach(func() {
		log.Printf("%v\n", CurrentGinkgoTestDescription())
		if CurrentGinkgoTestDescription().Failed {
			ConcatenatedMessage += failMessage
		}
	})
})
