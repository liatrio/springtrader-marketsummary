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

	Context("Step 3", func() {
		It("should have a CurrencyUtils.java file", func() {
			failMessage = "CurrencyUtils.java doesn't exist or is in the wrong location\n"
			Expect("../src/main/java/org/springframework/nanotrader/data/util/CurrencyUtils.java").To(BeAnExistingFile(), failMessage)
		})
	})

	Context("Step 4", func() {
		It("should have a MarketSummaryController.java file", func() {
			failMessage = "MarketSummaryController.java doesn't exist or is in the wrong location\n"
			Expect("../src/main/java/org/springframework/nanotrader/web/controller/MarketSummaryController.java").To(BeAnExistingFile(), failMessage)
		})
	})

	Context("Step 5", func() {
		It("should have a build.gradle file", func() {
			failMessage = "build.gradle doesn't exist or is in the wrong location\n"
			Expect("../build.gradle").To(BeAnExistingFile(), failMessage)
		})
	})

	AfterEach(func() {
		log.Printf("%v\n", CurrentGinkgoTestDescription())
		if CurrentGinkgoTestDescription().Failed {
			ConcatenatedMessage += failMessage
		}
	})
})
