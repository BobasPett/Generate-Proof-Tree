# Generate-Proof-Tree


***Algorithm***

**Input:** list of logical formulas called l in propositional logic

**Output:** semantic tableau

```python
Tableaux(formulas):
  if formulas contain contradiction:
        return True

    reapeat:
        if there exists a formula f w/ operator other than disjuntion in formulas:
            replace f w/ the matching logical equivalence

        else:
            break

    for each formula f in formulas:
        if f is a disjuntion:
            if Tableaux(left disjunct) and Tableaux(right disjunct):
                return True
  ```                
                  


