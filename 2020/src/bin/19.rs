use std::collections::HashMap;
use std::fs;
use std::str::FromStr;

#[derive(Debug)]
enum Rule {
    Char(char),
    Or(Box<Rule>, Box<Rule>),
    Seq(Vec<usize>),
}

impl Rule {
    /// Match a string assuming a rule matches a fixed length.
    ///
    /// This only works for part 1 of the problem. In part 2 two cyclic
    /// rules are introduced that don't have a well-defined length.
    ///
    /// The assumption that each rule maches a fixed number of characters
    /// allows us to make several simplifications:
    /// 1. Reject a string upfront if it doesn't match the length
    ///    of the rule.
    /// 2. If the rule is a sequence of sub-rules, then each sub-rule
    ///    corresponds to a well-defined sub-string of the input string.
    ///
    /// # Arguments
    /// * `s` -- The string to match.
    /// * `rules` -- A map of rule IDs to the rules.
    ///
    /// # Returns
    /// `bool` -- Whether or not the string matches the rule.
    fn simple_match(&self, s: &str, rules: &HashMap<usize, Rule>) -> bool {
        // Reject straight away if the lengths of the string and the rule don't match.
        if s.len() != self.len(rules) {
            return false;
        }
        match self {
            Rule::Char(c) => c == &s.chars().next().unwrap(),
            Rule::Seq(v) => {
                // Iterate over the rules in the sequence and match each of them. Because
                // Each sub-rule has a well-defined length we can track which sub-string
                // each sub-rule has to be applied to.
                let mut pos = 0;
                for rule in v.iter().map(|i| rules.get(i).unwrap()) {
                    let len = rule.len(rules);
                    if !rule.simple_match(&s[pos..pos + len], rules) {
                        return false;
                    }
                    pos += len;
                }
                // Because we've already done a length check at the beginning, we don't
                // need to check if the sequence exhausted the whole string, we already
                // know that it did.
                true
            }
            Rule::Or(r1, r2) => r1.simple_match(s, rules) | r2.simple_match(s, rules),
        }
    }

    fn advanced_match(&self, s: &str, rules: &HashMap<usize, Rule>) -> bool {
        match self {
            Rule::Char(c) => s.len() == 1 && c == &s.chars().next().unwrap(),
            Rule::Seq(v) => false,
            Rule::Or(r1, r2) => false,
        }
    }

    fn len(&self, rules: &HashMap<usize, Rule>) -> usize {
        match self {
            Rule::Char(_) => 1,
            Rule::Or(rule, _) => rule.len(rules),
            Rule::Seq(v) => v.iter().map(|x| rules.get(x).unwrap().len(rules)).sum(),
        }
    }
}

impl FromStr for Rule {
    type Err = ();

    fn from_str(line: &str) -> Result<Self, Self::Err> {
        // Starts with a double quote => Rule::Char
        if line.starts_with('"') {
            return Ok(Self::Char(line.chars().nth(1).unwrap()));
        }
        // Contains the pipe character => Rule::Or
        if line.find('|').is_some() {
            let mut pair: Vec<Box<Rule>> = line
                .split('|')
                .map(|s| Box::new(s.trim().parse().unwrap()))
                .collect();
            return Ok(Self::Or(pair.pop().unwrap(), pair.pop().unwrap()));
        }
        // Neither Rule::Char nor Rule::Or => Rule::Seq
        Ok(Self::Seq(
            line.split(' ').map(|x| x.parse().unwrap()).collect(),
        ))
    }
}

#[doc(hidden)]
fn main() {
    // Read input
    let data_blocks: Vec<String> = fs::read_to_string("input/19.txt")
        .expect("Can't read input")
        .split("\n\n")
        .map(|block| block.trim().to_string())
        .collect();
    let mut rules: HashMap<usize, Rule> = data_blocks[0]
        .lines()
        .map(|line| parse_rule(line))
        .collect();
    let messages: Vec<&str> = data_blocks[1].lines().collect();
    println!("line 0: {}", messages[0]);

    // Checks
    // Check that every rule matches a pattern of fixed length
    for rule in rules.values() {
        if let Rule::Or(r1, r2) = rule {
            assert_eq!(r1.len(&rules), r2.len(&rules));
        }
    }

    // Solutions
    // Find length of pattern 0
    println!("len(rule[0]) = {}", rules.get(&0).unwrap().len(&rules));

    // a = 123, b = 97
    let r = rules.get(&123).unwrap();
    println!(
        "does rule {:?} match 'a': {}",
        r,
        r.simple_match("a", &rules)
    );
    println!(
        "does rule {:?} match 'asdf': {}",
        r,
        r.simple_match("asdf", &rules)
    );

    let r = Rule::Seq(vec![123, 97]);
    println!(
        "does rule {:?} match 'ab': {}",
        r,
        r.simple_match("ab", &rules)
    );

    let r = Rule::Or(
        Box::new(Rule::Seq(vec![123, 97])),
        Box::new(Rule::Seq(vec![97, 123])),
    );
    println!(
        "does rule {:?} match 'ab': {}",
        r,
        r.simple_match("ab", &rules)
    );
    println!(
        "does rule {:?} match 'ba': {}",
        r,
        r.simple_match("ba", &rules)
    );

    let rule0 = rules.get(&0).unwrap();
    let part_1 = messages
        .iter()
        .filter(|message| rule0.simple_match(message, &rules))
        .count();
    println!("Part 1: {}", part_1);

    // Part 2
    // Replace 8th rule by "8: 42 | 42 8"
    rules.insert(
        8,
        Rule::Or(
            Box::new(Rule::Seq(vec![42])),
            Box::new(Rule::Seq(vec![42, 8])),
        ),
    );
    // Replace 11th rule by "11: 42 31 | 42 11 31"
    rules.insert(
        11,
        Rule::Or(
            Box::new(Rule::Seq(vec![42, 31])),
            Box::new(Rule::Seq(vec![42, 11, 31])),
        ),
    );
    let rule0 = rules.get(&0).unwrap();
    let part_2 = messages
        .iter()
        .filter(|message| rule0.advanced_match(message, &rules))
        .count();
    println!("Part 2: {}", part_2);
}

fn parse_rule(rule_s: &str) -> (usize, Rule) {
    let pair: Vec<&str> = rule_s.split(": ").collect();
    (pair[0].parse().unwrap(), pair[1].parse().unwrap())
}
