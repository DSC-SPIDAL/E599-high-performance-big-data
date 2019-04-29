#!/usr/bin/env python3

# -*- coding: utf-8 -*-
"""
Created on Fri Nov 30 23:19:48 2018

@author: nithish k
Please refer the report in the pdf commited in the git hub
"""

import pandas as pd
import numpy as np
import itertools as itr
import random
import collections as col
import math

import time 



impFeatures = []

class binaryNode():
    def __init__(self,data = None):
        self.left = None
        self.right = None
        self.data = data # as a dictionary of featurename, test threshold and prdiction class
        
        pass
    
    def insertLeft(self, node):
       
        self.left = node

    def insertRight(self,node):
        
        
        self.right = node
        
    
    def getLeftNode(self):
        
        return self.left
        
    
    def getRightNode(self):
        
        return self.right
        


class DecisionTree():
    def __init__(self,**kwargs):
        
#        numTrees = 20 , maxDepth = 5, numFeaturesInAtree = 14,baggingProportions = 2/3,verbose = False
        self.maxDepth = kwargs.get('maxDepth',5)
        self.numFeaturesInAtree =kwargs.get('numFeaturesInAtree',14) #mtry
        self.verbose  = kwargs.get('verbose',False)
        self.baggingProportions = kwargs.get('baggingProportions',2/3)
        self.TrainedTree = None
        self.featuresConsideredInTree = None
        self.isTrained = False
        
    def _calcSplittingEntropy(self,splitThreshold,featureValues,yValues):
        """
        calculates shanons entropy currently at median of the values in a columns
        
        """
        ##  /\
        ## + -
        ##pass filtered feature and yvalues
        LabelsInEachSplitCount = col.defaultdict(lambda: col.defaultdict(int))
        numTotalObs = len(featureValues)
        ##get positve splits
        booleanList = featureValues >= splitThreshold ##test criterion      
        positiveObs = featureValues[booleanList]
        numPositiveObs = len(positiveObs)
         
        ##get negative splits
        negatedBooleanList = np.logical_not(booleanList)
        negativeObs = featureValues[negatedBooleanList]
        numNegativeObs = len(negativeObs)
        
        ##segregate y Values
        positiveYValues = yValues[booleanList]
        negativeYValues = yValues[negatedBooleanList]
        
        LabelsInEachSplitCount['Positive'] = dict(col.Counter(positiveYValues))
        LabelsInEachSplitCount['Negative'] = dict(col.Counter(negativeYValues))
        
   
        ##calculate entropy
   
        positiveSideEntropy = sum([(-Labelcount/numPositiveObs)*math.log2(Labelcount/numPositiveObs)\
                                   for Labelcount in LabelsInEachSplitCount['Positive'].values()])
    
        
        negativeSideEntropy = sum([(-Labelcount/numNegativeObs)*math.log2(Labelcount/numNegativeObs)\
                                   for Labelcount in LabelsInEachSplitCount['Negative'].values()])
        
        
        
 
        weightedEntropy = (numPositiveObs/numTotalObs)*positiveSideEntropy + (numNegativeObs/numTotalObs)*negativeSideEntropy
        
        
        return weightedEntropy
    
    

        
    def _getBestSplit(self,filteredFeaturesNames,filteredObservations,filteredYvals):
        
        
        """
        gets the best split given a subset of features or subset of observations
        """
        entropyList = []
        
        subsetObsInFeatureNames = filteredObservations[:,filteredFeaturesNames]
        
        for featureNum, featureValues in zip(filteredFeaturesNames,subsetObsInFeatureNames.T):
#            splitThresholds = np.unique(featureValues)
            splitThresholds = np.int16(np.percentile(featureValues,[25,50,75]))
#            splitThresholds = [np.median(featureValues)]
#            splitThreshold = np.median(featureValues)
            for splitThreshold in splitThresholds:
                ##change this to random value
                
                entropyList.append((self._calcSplittingEntropy(splitThreshold,featureValues,filteredYvals),featureNum,splitThreshold))
                
        minEntropy,bestSplitFeatureName ,bestSplitThresh = min(entropyList)
        
        
        return minEntropy,bestSplitFeatureName ,bestSplitThresh
    
    
    def _createSplit(self,filteredObservations,bestSplitFeatureName,splitThreshold,FilteredYvals):
        """
        paritions data scoordint to the threshold and feature name
        """
        booleanList = filteredObservations[:,bestSplitFeatureName] >= splitThreshold
        positiveObservations = filteredObservations[booleanList,:]
        positiveYvals = FilteredYvals[booleanList]
        
        negatedBooleanList = np.logical_not(booleanList)
        negativeObservations = filteredObservations[negatedBooleanList,:]
        negativeYvals = FilteredYvals[negatedBooleanList]
        
        return positiveObservations,negativeObservations,positiveYvals,negativeYvals
    
    
    
    
    def _getDecisionFromTree(self,rootNode,oneObservation):
        """
        Implemented a BFS search to get the decision from a tree for one test example
        """
        ##returns decicion
        #{'featurename':None,'threshold':None,'prediction':majorityClass} 
        ##
        nodeList = [rootNode]
        while len(nodeList) > 0 :
            successorNode = nodeList.pop(0)

            leftNode = successorNode.getLeftNode()
            rightNode = successorNode.getRightNode()
            
            if rightNode is None and leftNode is None: ##reached terminal node
                prediction = successorNode.data['prediction']
                return prediction 
            
            testFeatureName = successorNode.data['featurename']
            testThreshHold = successorNode.data['threshold']
            
            testBoolean = oneObservation[testFeatureName] >= testThreshHold
            
            if leftNode is not None and testBoolean == True:
                nodeList.append(leftNode)
            
            if rightNode is not None and testBoolean == False:
                
                nodeList.append(rightNode)
            
             
        return "Failure" 
                
                
    
    def buildtree(self,filteredFeaturesNames,filteredObservations,filteredYvals,depth = 5):
        """
        This functions builds a single decision tree recurscively
        need to send all observations since arrays cannot be tracked by column name
        """
        ##returnss entire tree
            
            ##/\
            #+  -
          #true False  
        ##positive left and negative right
        
        uniqueValues = set(filteredYvals.tolist())
        
        if len(uniqueValues) == 1 :
            majorityClass, = uniqueValues
            decisionDict = {'featurename':None,'threshold':None,'prediction':majorityClass} 
            
            return binaryNode(decisionDict)
        
        if len(filteredFeaturesNames) == 0 or len(filteredYvals) == 0:
            return None
        

        ##getting the bestfeature to split and threshhold
        minEntropy,bestSplitFeatureName ,bestSplitThresh = self._getBestSplit(filteredFeaturesNames,
                                                                            filteredObservations,
                                                                            filteredYvals)
        
        
        ##segregating observations based on the test conditions

        positiveObservations,negativeObservations,positiveYvals,negativeYvals = \
        self._createSplit(filteredObservations,
                          bestSplitFeatureName,
                          bestSplitThresh,
                          filteredYvals)
        

        # as a dictionary of featurename, test threshold and prdiction class
        
        decisionDict = {'featurename':bestSplitFeatureName,'threshold':bestSplitThresh,'prediction':None} 
        
        if depth == 0:
            newTerminalNode = binaryNode(decisionDict)
            
            
            leftPrediction = col.Counter(positiveYvals).most_common(1)[0][0] if len(positiveYvals) > 0 else None
            rightPrediction = col.Counter(negativeYvals).most_common(1)[0][0] if len(negativeYvals) >0 else None
            
            LeftDecisionDict = {'featurename':None,'threshold':None,'prediction':leftPrediction}
            RightDecitionDict = {'featurename':None,'threshold':None,'prediction':rightPrediction}
            
            leftNode = binaryNode(LeftDecisionDict)
            rightNode = binaryNode(RightDecitionDict)
            
            newTerminalNode.insertLeft(leftNode)
            newTerminalNode.insertRight(rightNode)
            
            return newTerminalNode    
        
        
        LeftBranch =  self.buildtree(filteredFeaturesNames,positiveObservations,positiveYvals,depth-1)
        RightBranch = self.buildtree(filteredFeaturesNames,negativeObservations,negativeYvals,depth-1)
        
        
        rootNode = binaryNode(decisionDict)
        rootNode.insertLeft(LeftBranch)
        rootNode.insertRight(RightBranch)
        
        return rootNode
        
    def fit(self,TrainX,TrainY):
        numTotalFeatures = TrainX.shape[1]
        numObservations = TrainX.shape[0]
        
        Allfeatures = list(range(numTotalFeatures))
        random.shuffle(Allfeatures)
        
        
        randomFeaturesForATree = Allfeatures[:self.numFeaturesInAtree]
        
        
        decisionTree = self.buildtree(randomFeaturesForATree,
                       TrainX,TrainY,self.maxDepth)
        self.featuresConsideredInTree = randomFeaturesForATree
        self.TrainedTree = decisionTree
        
        return self
    
    def predict(self,TestX):
        decisionTree = self.TrainedTree
        finalPredictionList = []
        
        
        for row in TestX:
 
            predictionForRow = self._getDecisionFromTree(decisionTree,row)
            finalPredictionList.append(predictionForRow)
         
        return np.array(finalPredictionList)
        
        
    
    def writeToFile(self,ID,predictionList,filename):
        OutputDf = pd.DataFrame({'ID':ID,'Predictions':predictionList})
        OutputDf.to_csv(path_or_buf = filename ,sep = ' ',header = False ,index = False)
        
    def verbosePrint(self,*args):
        if self.verbose:
            print(*args) 
        
        ###to be removed
    def getDataFromFile(self, filename):
    
        DataDf = pd.read_csv(filename,header = None, sep = ' ' )
        DataDf.columns = ['photo_id','correct_orientation'] + [i-2 for i  in DataDf.columns[2:].tolist()]
    #        self.trainData = trainDataDf
        XDataMatrix = np.array(DataDf.loc[:,~DataDf.columns.isin(['photo_id','correct_orientation'])])
        YLabels = DataDf['correct_orientation']
        XDataID = DataDf['photo_id']
        return XDataMatrix,YLabels,XDataID
    
    
    
if __name__ == "__main__":

    
    
    myTree = DecisionTree(maxDepth=5,verbose = True)
    TrainX,TrainY,TrainXID= myTree.getDataFromFile('train-data.txt')
    Xtest,yTest,XtestID = myTree.getDataFromFile('test-data.txt')
    start = time.time()
    
    
    finalPredictions = myTree.fit(TrainX,TrainY).predict(Xtest)
    
    print("Time elapsed is ", time.time()-start)
    print("Accuracy is: " ,sum(finalPredictions==yTest)/len(yTest))
    
    

    