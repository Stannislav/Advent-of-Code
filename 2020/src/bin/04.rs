extern crate regex;

use regex::Regex;
use std::collections::HashMap;
use std::fs;

/// Parse a passport entry.
///
/// First we extract a vector with all key-value pairs of the form
/// `key:value` by splitting the data on spaces and new lines. Then
/// all such pairs are split on `:` and inserted into a hash map.
///
/// # Arguments
/// * `passport_data` -- Part of the input data representing one passport.
///
/// # Returns
/// * `HashMap<String, String>` -- The parsed passport data as a key-value map.
fn parse_passport(passport_data: &str) -> HashMap<String, String> {
    let entries: Vec<String> = passport_data
        .trim()
        .to_string()
        .split(|x| x == '\n' || x == ' ')
        .map(|x| x.to_string())
        .collect();

    let mut passport: HashMap<String, String> = HashMap::new();
    for entry in entries.iter() {
        let pair: Vec<&str> = entry.split(':').collect();
        passport.insert(pair[0].to_string(), pair[1].to_string());
    }

    passport
}

/// Check passport for part 1 and 2.
///
/// For part one (`check_values = false`) we only need to check for
/// the presence of all required keys in the hash map.
///
/// For part two (`check_values = true`) we need to perform a specific
/// check for each of the values. The requirements are stated in the
/// problem text as follows:
///
/// * byr (Birth Year) -- four digits; at least 1920 and at most 2002.
/// * iyr (Issue Year) -- four digits; at least 2010 and at most 2020.
/// * eyr (Expiration Year) -- four digits; at least 2020 and at most 2030.
/// * hgt (Height) -- a number followed by either cm or in:
///   * If `cm`, the number must be at least 150 and at most 193.
///   * If `in`, the number must be at least 59 and at most 76.
/// * hcl (Hair Color) -- a # followed by exactly six characters 0-9 or a-f.
/// * ecl (Eye Color) -- exactly one of: `amb` `blu` `brn` `gry` `gr`n `hzl` `oth`.
/// * pid (Passport ID) -- a nine-digit number, including leading zeroes.
/// * cid (Country ID) -- ignored, missing or not.
///
/// # Arguments
/// * `passport` -- Passport data produced by [parse_passport](parse_passport).
/// * `check_values` -- If `false` then only the keys are checked (part 1),
///   otherwise also the values (part 2).
///
/// # Returns
/// * `u8` -- Return 1 if the check passes, otherwise 0;
fn check_passport(passport: &HashMap<String, String>, check_values: bool) -> u8 {
    for mandatory_field in ["byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid"].iter() {
        if !passport.contains_key(&mandatory_field.to_string()) {
            return 0;
        }
    }

    if !check_values {
        return 1;
    }

    // Part 2 only
    // byr
    let byr: i32 = passport.get("byr").unwrap().parse().unwrap();
    if byr < 1920 || byr > 2002 {
        return 0;
    }
    // iyr
    let iyr: i32 = passport.get("iyr").unwrap().parse().unwrap();
    if iyr < 2010 || iyr > 2020 {
        return 0;
    }
    // eyr
    let eyr: i32 = passport.get("eyr").unwrap().parse().unwrap();
    if eyr < 2020 || eyr > 2030 {
        return 0;
    }
    // hgt
    let hgt = passport.get("hgt").unwrap();
    let re = Regex::new(r"^(\d+)(cm|in)$").expect("Broken regex");
    if !re.is_match(hgt) {
        return 0;
    }
    let caps = re.captures(hgt).unwrap();
    let n: i32 = (&caps[1]).parse().unwrap();
    let unit: &str = &caps[2];
    if !((unit == "cm" && n >= 150 && n <= 193) || (unit == "in" && n >= 59 && n <= 76)) {
        return 0;
    }
    // hcl
    let hcl = passport.get("hcl").unwrap();
    let re = Regex::new(r"^#[0-9a-f]{6}$").expect("Broken regex");
    if !re.is_match(hcl) {
        return 0;
    }
    // ecl
    let ecl = passport.get("ecl").unwrap();
    let colors = vec!["amb", "blu", "brn", "gry", "grn", "hzl", "oth"];
    if !colors.contains(&ecl.as_str()) {
        return 0;
    }
    // pid
    let pid = passport.get("pid").unwrap();
    let re = Regex::new(r"^\d\d\d\d\d\d\d\d\d$").expect("Broken regex");
    if !re.is_match(pid) {
        return 0;
    }

    return 1;
}

#[doc(hidden)]
fn main() {
    // Read data
    let passports: Vec<HashMap<String, String>> = fs::read_to_string("input/04.txt")
        .expect("Can't read input file.")
        .split("\n\n")
        .map(parse_passport)
        .collect();

    // Solutions
    let mut part_1 = 0;
    let mut part_2 = 0;
    for passport in passports.iter() {
        part_1 += check_passport(passport, false);
        part_2 += check_passport(passport, true);
    }
    println!("Part 1: {}", part_1);
    println!("Part 2: {}", part_2);
}
