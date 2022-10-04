class OrderTicket:
    def __init__(self):
        self.id = 0
        self.timestamp = 0
        self.customerFirstName = ''
        self.rewardsMemberId = 0
        self.employeeId = 0

class OrderItem:
    def __init__(self):
        self.id = 0
        self.orderId = 0
        self.itemNumberInOrder = 0
        self.itemName = 0
        self.itemAmount = 0
        self.itemSize = 0

class OrderItemModification:
    def __init__(self):
        self.id = 0
        self.orderId = 0
        self.itemNumberInOrder = 0
        self.ingredientId = 0

class OrderItemAdditiopn(OrderItemModification):
    pass

class OrderItemSubtraction(OrderItemModification):
    pass

class Product:
    def __init__(self):
        self.id = 0
        self.name = ''
        self.price = 0.0

class Ingredient:
    def __init__(self):
        self.id = 0
        self.name = ''
        self.expirationDate = 0 # date
        self.quantityRemaining = 0.0
        self.measurementUnits = ''
        self.pricePerUnitLastOrder = 0.0
        self.lastOrderDate = 0 # date
        self.unitsInLastOrder = 0.0

class ProductToIngredient:
    def __init__(self):
        self.productId = 0
        self.ingredientId = 0

daysOfSales = 28
totalSales = 20_000
numberOfGameDays = 2
minUniqueItems = 20