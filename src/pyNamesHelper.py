import random, os

namesPath = '/Users/pg/github/local/NamesML/names/'
trainingPath = '/Users/pg/github/local/NamesML/training/'
validatePath = '/Users/pg/github/local/NamesML/validation/'

def extractTrainingSet(file, percent):
    fd_in = open(namesPath + file, 'r')
    fd_train = open(trainingPath + file, 'w')
    fd_validate = open(validatePath + file, 'w')
    train_count = 0
    validate_count = 0
    for line in fd_in:
        name = line.split('\t')[0].lower()
        if random.uniform(0,100) < percent:
            fd_train.write(name + '\n')
            train_count = train_count + 1
        else:
            fd_validate.write(name + '\n')
            validate_count = validate_count + 1
    fd_train.flush()
    fd_train.close()
    fd_validate.flush()
    fd_validate.close()
    print 'written training file containing ' + str(train_count) + ' names to ' + trainingPath + file
    print 'written validation file containing ' + str(validate_count) + ' names to ' + validatePath + file

if __name__ == '__main__':
    namesDir = os.listdir(namesPath)
    pct = 75
    for f in namesDir:
        extractTrainingSet(f, pct)    
