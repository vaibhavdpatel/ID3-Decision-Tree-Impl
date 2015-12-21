# ID3-Decision-Tree-Impl
Implementation of ID3 Decision Tree (Machine Learning algorithm) in Java. External library used: Weka

The program expectes: three command-line arguments as follows:
<train-set-file> <test-set-file> m

The stopping criteria (for making a node into a leaf) are that 
(i) all of the training instances reaching the node belong to the same class, or 
(ii) there are fewer than m training instances reaching the node, where m is provided as input to the program, or 
(iii) no feature has positive information gain, or 
(iv) there are no more remaining candidate splits at the node.
