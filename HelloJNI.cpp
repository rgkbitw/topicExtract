#include <iostream>
#include<cstdlib>
#include "HelloJNI.h"
using namespace std;

JNIEXPORT void JNICALL Java_HelloJNI_sayHello( JNIEnv *env, jobject thisObj )
{
	system( "cd mallet && ./bin/mallet import-dir --input Data --output tutorial.mallet --keep-sequence --remove-stopwords && ./bin/mallet train-topics --input tutorial.mallet --num-topics 3 --optimize-interval 20 --output-state topic-state.gz --output-topic-keys tutorial_keys.txt --output-doc-topics tutorial_composition.txt" );
    	return;
}
