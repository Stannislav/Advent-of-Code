use std::collections::HashMap;
use std::fs;
use std::str::FromStr;

#[derive(Debug)]
/// Representation of a rule from the problem input.
enum Rule {
    Char(char),
    Or(Box<Rule>, Box<Rule>),
    Seq(Vec<usize>),
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
    fn matches(&self, s: &str, rules: &HashMap<usize, Rule>) -> bool {
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
                    if !rule.matches(&s[pos..pos + len], rules) {
                        return false;
                    }
                    pos += len;
                }
                // Because we've already done a length check at the beginning, we don't
                // need to check if the sequence exhausted the whole string, we already
                // know that it did.
                true
            }
            Rule::Or(r1, r2) => r1.matches(s, rules) | r2.matches(s, rules),
        }
    }

    /// Compute the length of the message that the rule matches.
    ///
    /// This assumes that all rules of type `Rule::Or` have alternatives
    /// with equal lengths.
    ///
    /// # Arguments
    /// * `rules` -- A map of rule IDs to the rules.
    ///
    /// # Returns
    /// `usize` -- The computed length.
    fn len(&self, rules: &HashMap<usize, Rule>) -> usize {
        match self {
            Rule::Char(_) => 1,
            Rule::Or(rule, _) => rule.len(rules),
            Rule::Seq(v) => v.iter().map(|x| rules.get(x).unwrap().len(rules)).sum(),
        }
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
    let rules: HashMap<usize, Rule> = data_blocks[0]
        .lines()
        .map(|line| {
            let pair: Vec<&str> = line.split(": ").collect();
            (pair[0].parse().unwrap(), pair[1].parse().unwrap())
        })
        .collect();
    let messages: Vec<&str> = data_blocks[1].lines().collect();

    // Check that every rule matches a pattern of fixed length
    for rule in rules.values() {
        if let Rule::Or(r1, r2) = rule {
            assert_eq!(r1.len(&rules), r2.len(&rules));
        }
    }

    // Part 1
    let rule0 = rules.get(&0).unwrap();
    let part_1 = messages
        .iter()
        .filter(|message| rule0.matches(message, &rules))
        .count();
    println!("Part 1: {}", part_1);

    // Part 2
    // We can solve this part for the specific case that is the input data.
    // We are asked to modify rules 8 and 11 in the following way:
    //     8: 42 | 42 8
    //     11: 42 31 | 42 11 31
    // We can expand the recursion of these rules and get the following:
    //     8 = (n * 42)
    //     11 = (m * 42) (m * 31)
    // For some integers n and m that are greater or equal to 1.
    //
    // Furhtermore, we realize that these recursive rules only appear in one place,
    // namely in rule 0:
    //    0: 8 11
    // After substituting the expansion of the rules 8 and 11 we get
    //    0: (n * 42) (m * 42) (m * 31)
    // Exploiting the fact that rules 42 and 31 match messages of a fixed length we
    // can iterate through all possible values for n and m for which the cumulative
    // length of the pattern matches that of the message. What we need to check is
    // whether any these combinations give a rule that matches the message.
    let mut count = 0;
    let len42 = rules.get(&42).unwrap().len(&rules);
    let len31 = rules.get(&31).unwrap().len(&rules);
    let len8 = len42; // for n = 1
    let len11 = len42 + len31; // for m = 1
    for message in messages.iter() {
        for n in 1..message.len() / len8 {
            let m = (message.len() - n * len8) / len11;
            if m == 0 || n * len8 + m * len11 != message.len() {
                continue;
            }
            // We found a valid (n, m) pair, now build the rule.
            let mut rule0_vec: Vec<usize> = vec![42; n];
            rule0_vec.extend(vec![42; m]);
            rule0_vec.extend(vec![31; m]);
            let rule0 = Rule::Seq(rule0_vec);

            // Match the rule agains the message.
            if rule0.matches(message, &rules) {
                count += 1;
                break;
            }
        }
    }
    println!("Part 2: {}", count);
}
