import os
from sklearn.neural_network import MLPClassifier
from sklearn.datasets import fetch_openml




Xdata, ydata = fetch_openml('mnist_784', version=1, return_X_y=True)

#setting up dask cluster
from dask.distributed import Client,progress, wait
client = Client('149.165.148.24:8786')
print(client)

X, y = Xdata / 255., ydata
X_train, X_test = X[:60000], X[60000:]
y_train, y_test = y[:60000], y[60000:]

mlp = MLPClassifier(hidden_layer_sizes=(100,10),max_iter=10,solver='sgd', verbose=10, random_state=1)

from sklearn.externals import joblib
with joblib.parallel_backend('dask', scheduler_host='149.165.148.24:8786'): 
    get_ipython().run_line_magic('time', 'mlp.fit(X_train, y_train)')

print("Training set score: %f" % mlp.score(X_train, y_train))
print("Test set score: %f" % mlp.score(X_test, y_test))


# ### With 100 iterations

mlp = MLPClassifier(hidden_layer_sizes=(100,10),max_iter=100,solver='sgd', verbose=10, random_state=1)
from sklearn.externals import joblib
with joblib.parallel_backend('dask', scheduler_host='149.165.148.24:8786'): 
    get_ipython().run_line_magic('time', 'mlp.fit(X_train, y_train)')
print("Training set score: %f" % mlp.score(X_train, y_train))
print("Test set score: %f" % mlp.score(X_test, y_test))


# ### with 1000 iterations

mlp = MLPClassifier(hidden_layer_sizes=(100,10),max_iter=1000,solver='sgd', verbose=10, random_state=1)
from sklearn.externals import joblib
with joblib.parallel_backend('dask', scheduler_host='149.165.148.24:8786'): 
    get_ipython().run_line_magic('time', 'mlp.fit(X_train, y_train)')
print("Training set score: %f" % mlp.score(X_train, y_train))
print("Test set score: %f" % mlp.score(X_test, y_test))


# ### with 10000 iterations
mlp = MLPClassifier(hidden_layer_sizes=(100,10),max_iter=10000,solver='sgd', verbose=10, random_state=1)
from sklearn.externals import joblib
with joblib.parallel_backend('dask', scheduler_host='149.165.148.24:8786'): 
    get_ipython().run_line_magic('time', 'mlp.fit(X_train, y_train)')
print("Training set score: %f" % mlp.score(X_train, y_train))
print("Test set score: %f" % mlp.score(X_test, y_test))


