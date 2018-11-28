The project should simply build and run. The output is a representation of the decision tree that is created. The following is the problem description: 

# Decision Trees
Implement an ID3 decision tree learner to teach a program how to classify DNA.

Splice junctions are points on a DNA sequence at which `superfluous' DNA is removed during the process of protein creation in higher organisms. The problem posed in this dataset is to recognize, given a sequence of DNA, the boundaries between exons (the parts of the DNA sequence retained after splicing) and introns (the parts of the DNA sequence that are spliced out). This problem consists of two subtasks: recognizing exon/intron boundaries (referred to as EI sites), and recognizing intron/exon boundaries (IE sites). (In the biological community, IE borders are referred to as ``acceptors'' while EI borders are referred to as ``donors''.)

Attributes predicted: given a position in the middle of a window 60 DNA sequence elements (called "nucleotides" or "base-pairs"), decide if this is a

a) "intron -> exon" boundary (IE) [These are sometimes called "donors"] <br />
b) "exon -> intron" boundary (EI) [These are sometimes called "acceptors"] <br />
c) neither (N)
