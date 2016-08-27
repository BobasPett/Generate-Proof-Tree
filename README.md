# Generate-Proof-Tree

***Algorithm***

**Input:** list of logical formulas in propositional logic

**Output:** semantic tableau

```
Tableaux(formulas)
    IF formulas contain contradiction
        RETURN TRUE
    REPEAT
        IF there exists a formula F with operator other than disjuntion IN formulas
            replace F with the matching logical equivalence
        ELSE
            BREAK
    FOR each formula F IN formulas
        IF F is a disjuntion AND Tableaux(F.leftDisjunct) AND Tableaux(F.rightDisjunct)
            RETURN TRUE
```


