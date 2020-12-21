//! Solutions for day 21.
use std::collections::{BTreeMap, HashMap, HashSet};
use std::fs;

#[doc(hidden)]
fn main() {
    // Read data
    let data: Vec<(HashSet<String>, HashSet<String>)> = fs::read_to_string("input/21.txt")
        .expect("Can't read input")
        .trim()
        .lines()
        .map(|line| parse_line(line))
        .collect();

    // Collect all possible allergens
    let all_allergens: HashSet<String> = data
        .iter()
        .flat_map(|(_ingredients, allergens)| allergens.clone())
        .collect();

    // Make a map from allergens to the possible ingredients that can contain them
    // This is done by choosing an allergen and finding all lines in the input that
    // contain this allergen. Since this allergen must be contained in every such list
    // of ingredients, all possible candidate ingredients for this allergen are given
    // by the intersection of these lists. For example, if the only lines containing
    // the allergen "dairy" are the following ones:
    //
    //     mxmxvkd kfcds sqjhc fvjkl (contains dairy, fish)
    //     trh fvjkl sbzzf mxmxvkd (contains dairy)
    //
    // then "dairy" must be one of the following two: {"mxmxvkd", "fvjkl"} since they
    // are the only ones that are present in both lines. Here we're building a map from
    // all allergens to sets of potential candidate ingredients like shown above.
    let mut allergen_candidates: HashMap<String, HashSet<String>> = HashMap::new();
    for allergen in all_allergens.iter() {
        // Collect all lines with a given allergen
        let candidates: Vec<&HashSet<String>> = data
            .iter()
            .filter(|(_ingredients, allergens)| allergens.contains(allergen))
            .map(|(ingredients, _allergens)| ingredients)
            .collect();
        // Compute the intersection of these lines
        let candidates: HashSet<String> = candidates
            .iter()
            .skip(1)
            .fold(candidates[0].clone(), |acc, c| {
                acc.intersection(c).cloned().collect()
            });
        // Insert the results into a hash map
        allergen_candidates.insert(allergen.clone(), candidates);
    }

    // Find those allergens that are uniquely identified and remove them from the
    // other candidates. An allergen is uniquely identified if it has only one
    // candidate ingredient. This also means that this ingredient can't contain any
    // other allergen, and we should remove it from the sets of candidate ingredients
    // for other allergens.
    //
    // Once we have removed the identified ingredient from other allergen candidates
    // we might get new allergens that are now uniquely identified. We can repeat
    // this procedure until no more allergens can be uniquely identified. It turns out
    // that this actually identifies all allergens uniquely.
    //
    // We're using a BTreeMap instead of a HashMap because in a BTreeMap the keys are
    // automatically sorted, this is useful for part 2.
    let mut identified: BTreeMap<String, String> = BTreeMap::new();
    loop {
        // Find all allergens that are uniquely identified and the corresponding ingredients.
        let new_identified: HashMap<String, String> = allergen_candidates
            .iter()
            .filter(|(_allergen, candidates)| candidates.len() == 1)
            .map(|(allergen, candidates)| {
                (allergen.clone(), candidates.iter().next().unwrap().clone())
            })
            .collect();
        // We stop if no more new allergens can be identified.
        if new_identified.is_empty() {
            break;
        }
        // Process the allergs that are uniquely identified
        for (allergen, ingredient) in new_identified.iter() {
            // This allergen has already been identified and will be recoreded in
            // `identified` below, we should remove it from the candidates map to
            // avoid it being processed again.
            allergen_candidates.remove(allergen);
            // Remove the corresponding ingredients from other allergen candidates
            for (_other_allergen, candidates) in allergen_candidates.iter_mut() {
                candidates.remove(ingredient);
            }
        }
        // Save all allegens identified in this iteration
        identified.extend(new_identified);
    }

    // To solve part 1 we need to find all ingredients that cannot contain
    // allergens. So for each line of ingredients from the input file we
    // remove those that contain allergens (`bad_ingredients`) and count
    // how many good ingredients remain.
    let bad_ingredients: HashSet<String> = identified
        .values()
        .map(|ingredient| ingredient.to_string())
        .collect();
    let part_1: usize = data
        .iter()
        .map(|(ingredients, _allergens)| {
            ingredients
                .difference(&bad_ingredients)
                .collect::<HashSet<_>>()
                .len()
        })
        .sum();
    println!("Part 1: {}", part_1);

    // Part 2
    // In fact, we've already solved it while solving part 1. Now we
    // just need to sort all allergens alphabetically and concatenate
    // the corresponding ingredients by a comma. Note that since we used
    // a BTreeMap, the keys in `identified` are already sorted.
    let part_2: String = identified
        .values()
        .map(|ingredient| ingredient.to_string())
        .collect::<Vec<String>>()
        .join(",");
    println!("Part 2: {}", part_2);
}

/// Parse one line of the input file.
///
/// An input file could have the following contents:
///
///     mxmxvkd kfcds sqjhc nhms (contains dairy, fish)
///     trh fvjkl sbzzf mxmxvkd (contains dairy)
///     sqjhc fvjkl (contains soy)
///     sqjhc mxmxvkd sbzzf (contains fish)
///
/// Each line contains a list of ingredients and in parentheses
/// a list of allergens that some of these ingredients contain. This
/// function extracts the ingredients and the allergens and stores
/// them in two sets. For example, the first line would give the
/// following two sets:
///
///     {"mxmxvkd", "kfcds", "sqjhc", "nhms"}
///     {"dairy", "fish"}
///
/// # Arguments
/// * `line` : One line from the input file.
///
/// # Returns
/// * `HashSet<String>` : The set of ingredients
/// * `HashSet<String>` : The set of allergens
fn parse_line(line: &str) -> (HashSet<String>, HashSet<String>) {
    // Split on the position of the opening parenthesis
    let bracket = line.find('(').unwrap();
    let ingredients: HashSet<String> = line[..bracket]
        .trim()
        .split(' ')
        .map(|s| s.to_string())
        .collect();
    let allergens: HashSet<String> = line[bracket + 1 + "contains".len()..line.len() - 1]
        .trim()
        .split(", ")
        .map(|s| s.to_string())
        .collect();
    (ingredients, allergens)
}
