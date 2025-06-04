package main

type Module interface {
	Receive(from string, value bool) (signal bool, send bool)
}

type FlipFlop struct {
	State bool
}

func NewFlipFlop() *FlipFlop {
	return &FlipFlop{false}
}

func (f *FlipFlop) Receive(from string, value bool) (signal bool, send bool) {
	println("FlipFlop received value:", value)
	if value {
		return false, false
	} else {
		f.State = !f.State
		return f.State, true
	}
}

func (f *FlipFlop) String() string {
	return "FlipFlop"
}

type Conjunction struct {
	LastInputs map[string]bool
}

func NewConjunction(inputs []string) *Conjunction {
	lastInputs := make(map[string]bool)
	for _, input := range inputs {
		lastInputs[input] = false
	}
	return &Conjunction{lastInputs}
}

func (c *Conjunction) Receive(from string, value bool) (signal bool, doSend bool) {
	println("Conjunction received value:", value)
	c.LastInputs[from] = value
	// If all inputs are true, then send false, otherwise true.
	signal = false
	for _, v := range c.LastInputs {
		if !v {
			signal = true
			break
		}
	}
	return signal, true
}

func (c *Conjunction) String() string {
	return "Conjunction"
}

type Broadcast struct{}

func (b *Broadcast) Receive(from string, value bool) (signal bool, doSend bool) {
	println("Broadcast received value:", value)
	return value, true
}

func (b *Broadcast) String() string {
	return "Broadcast"
}
