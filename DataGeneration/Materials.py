import random
import datetime

import Utils

import numpy as np
class Material:
    numberOfMaterials = 0
    
    def __init__(self):
        self.id = Material.numberOfMaterials
        Material.numberOfMaterials += 1

        self.name = ''
        self.size = ''
        self.quantityRemaining = 0
        self.measurementsUnits = ''
        self.itemsPerUnit = 0
        self.pricePerUnitLastOrder = 0
        self.lastOrderDate = '' # date
        self.unitsInLastOrder = 0.0

def generateMaterials():
    materials = []
    materialNames = ['toilet paper', 'napkins', 'straws', 'cups', 'receipt paper']
    for materialName in materialNames:
        material = Material()
        material.name = materialName
        material.size = random.choice(['small', 'medium', 'large'])
        material.quantityRemaining = random.choice(range(1, 30))
        material.measurementsUnits = 'units'
        material.itemsPerUnit = random.choice(range(1, 10))
        material.pricePerUnitLastOrder = round(random.choice(np.arange(5, 25, .01)), 2)
        material.lastOrderDate = Utils.getRandomDate(datetime.date(2022, 9, 1), datetime.date(2022, 10, 1))
        material.unitsInLastOrder = round(random.choice(np.arange(1, 30, .1)), 2)
        materials.append(material)
    return materials

if __name__ == '__main__':
    materials = generateMaterials()
    Utils.writeObjectsToCsv(materials, 'data/Materials.csv')