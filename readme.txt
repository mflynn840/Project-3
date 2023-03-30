To build and run:

	For exact inference:

		1) javac ExactInferencer.java

		2) java ExactInferencer.java [Examples/FILE NAME.xml] [Query variable name] [evidence 1] [value 1].... 			[evidence n] [value n]

	For Rejection sampling:

		1) javac RejectionSampler.java
		
		2) java RejectionSampler.java [numSamples] [Examples/FILE NAME.xml] [Query variable name] [evidence 1] 			[value 1] ... [Evidence n] [value n]
		

	For likelyhood weighting:

		1) javac LikelyhoodWeighter.java 

		2) java LikelyhoodWeighter.java [numSamples] [Examples/FILE NAME.xml] [Query variable name] [evidence 1] 			[value 1] ... [evidence n] [value n]



