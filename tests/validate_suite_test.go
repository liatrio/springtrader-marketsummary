package main

import (
	"fmt"
	"os"
	"testing"

	. "github.com/onsi/ginkgo"
	. "github.com/onsi/gomega"
)

var ConcatenatedMessage string

func TestValidate(t *testing.T) {
	RegisterFailHandler(Fail)
	RunSpecs(t, "Validate Suite")
}

var _ = AfterSuite(func() {
	f, err := os.Create("slack_output.md")
	if err != nil {
		fmt.Println(err)
		return
	}

	_, err = f.WriteString(ConcatenatedMessage)

	if err != nil {
		fmt.Println(err)
	}

	f.Close()

})
