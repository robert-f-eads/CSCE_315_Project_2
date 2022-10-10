import random
from typing import List
import datetime

import RewardsMembers
import Employees
import Utils
import ProductsIngredients

class OrderTicket(Utils.StringRepresentation):
    numberOfOrderTickets = 0
    def __init__(self):
        self.id = OrderTicket.numberOfOrderTickets
        OrderTicket.numberOfOrderTickets += 1

        self.timestamp = 0
        self.customerFirstName = ''
        self.rewardsMemberId = 0
        self.employeeId = 0
        self.orderPriceTotal = 0.0

# day is a datetime date
def generateOrderTickets(numToGenerate, day: datetime.datetime, customers: List[RewardsMembers.RewardMember], employees: List[Employees.Employee]):
    orderTickets: List[OrderTicket] = []
    timestamps = [day + random.random() * datetime.timedelta(days=1) for i in range(numToGenerate)]
    for timestamp in timestamps:
        orderTicket = OrderTicket()
        orderTicket.timestamp = timestamp.strftime("%Y-%m-%d %H:%M:%S")
        customer = random.choice(customers)
        employee = random.choice(employees)
        orderTicket.customerFirstName = customer.firstName
        orderTicket.rewardsMemberId = customer.id
        orderTicket.employeeId = employee.id
        orderTickets.append(orderTicket)
    return orderTickets


class OrderItem:
    numberOfOrderItems = 0

    def __init__(self, orderId, itemNumberInOrder, itemName, itemAmount, itemSize):
        self.id = OrderItem.numberOfOrderItems
        OrderItem.numberOfOrderItems += 1

        self.orderId = orderId
        self.itemNumberInOrder = itemNumberInOrder
        self.itemName = itemName
        self.itemAmount = itemAmount
        self.itemSize = itemSize

class OrderItemModification:
    def __init__(self):
        self.orderId = 0
        self.itemNumberInOrder = 0
        self.ingredientId = 0

class OrderItemAddition(OrderItemModification):
    numberOfOrderItemAdditions = 0
    def __init__(self):
        self.id = OrderItemAddition.numberOfOrderItemAdditions
        OrderItemAddition.numberOfOrderItemAdditions += 1
        super()

class OrderItemSubtraction(OrderItemModification):
    numberOfOrderItemSubtractions = 0
    def __init__(self):
        self.id = OrderItemSubtraction.numberOfOrderItemSubtractions
        OrderItemSubtraction.numberOfOrderItemSubtractions += 1
        super()

if __name__ == '__main__':
    daysOfSales = 28
    # we aim for 20k of sales per week
    totalSales = int(20000 * daysOfSales / 7) # dollars, this is just approx, not guarenteed to be over this number
    numberOfGameDays = 2
    avgOrderAmount = 20 # dollars, helpful for determining how many orders are in a day given that day has x dollars in sales

    avgSalesPerDay = totalSales / daysOfSales
    gameDaySales = [random.randint(int(avgSalesPerDay * 5), int(avgSalesPerDay * 7)) for i in range(numberOfGameDays)]
    typicalDaySales = [random.randint(int(avgSalesPerDay * .5), int(avgSalesPerDay * 1.5)) for i in range(daysOfSales)]

    # splice in two days to be game days
    gameDays = []
    for i in range(numberOfGameDays):
        randomDay = random.randint(0, daysOfSales)
        while randomDay in gameDays:
            randomDay = random.randint(0, daysOfSales)
        gameDays.append(randomDay)

    allDaySales = typicalDaySales.copy()
    for gameDay, gameDaySale in zip(gameDays, gameDaySales):
        allDaySales.insert(gameDay, gameDaySale)

    customers = RewardsMembers.generateRewardsMembers(100)
    employees = Employees.generateEmployees(10)
    products = ProductsIngredients.loadProductsFromJson()
    avgProductPrice = sum([humanReadableProduct.product.price for humanReadableProduct in products]) / len(products)
    ingredients = ProductsIngredients.loadIngredientsByScraping()
    ingredientsNames = [ingredient.name for ingredient in ingredients]

    currentSaleDay = datetime.datetime(2022, 9, 1) # arbitrary day to start selling
    orderTickets: List[OrderTicket] = []
    orderItems: List[OrderItem] = []
    orderItemAdditions: List[OrderItemAddition] = []
    orderItemSubtractions: List[OrderItemAddition] = []

    for daySale in allDaySales:
        numOrders = daySale // avgOrderAmount
        currentSaleDay += datetime.timedelta(days=1)
        print(daySale, ':', currentSaleDay)
        dayOrderTickets = generateOrderTickets(numOrders, currentSaleDay, customers, employees)
        for dayOrderTicket in dayOrderTickets:
            ticketOrderAmount = random.randint(int(avgOrderAmount * .5), int(avgOrderAmount * 1.5))
            numProductsOnTicket = int(ticketOrderAmount // avgProductPrice)
            # accumulate the price of the ticket with this
            dayOrderTicketPrice = 0
            for i in range(numProductsOnTicket):
                orderItemProduct = random.choice(products)
                numberOfProductPurchased = random.choice(range(1, 3))
                dayOrderTicketPrice += numberOfProductPurchased * orderItemProduct.product.price
                # sizes are in oz for orderItem
                orderItem = OrderItem(dayOrderTicket.id, i, orderItemProduct.product.name, numberOfProductPurchased, random.choice([20, 32, 40]))
                orderItems.append(orderItem)

                numOrderAdditions = random.randint(0, 2)
                for j in range(numOrderAdditions):
                    orderItemAddition = OrderItemAddition()
                    orderItemAddition.ingredientId = random.choice(ingredients).id
                    orderItemAddition.orderId = dayOrderTicket.id
                    orderItemAddition.itemNumberInOrder = i
                    orderItemAdditions.append(orderItemAddition)
                numOrderSubtractions = random.randint(0, 2)
                for j in range(numOrderSubtractions):
                    orderItemSubtraction = OrderItemSubtraction()

                    # an ingredient to subtract must be in the product that was ordered
                    ingredientNameToSubtract = random.choice(orderItemProduct.ingredients)
                    orderItemSubtraction.ingredientId = ingredients[ingredientsNames.index(ingredientNameToSubtract)].id

                    orderItemSubtraction.orderId = dayOrderTicket.id
                    orderItemSubtraction.itemNumberInOrder = i
                    orderItemSubtractions.append(orderItemSubtraction)
            dayOrderTicket.orderPriceTotal = dayOrderTicketPrice
        orderTickets.extend(dayOrderTickets)

    Utils.writeObjectsToCsv(orderTickets, 'data/OrderTickets.csv')
    Utils.writeObjectsToCsv(orderItems, 'data/OrderItems.csv')
    Utils.writeOrderItemModificationToCsv(orderItemAdditions, 'data/OrderItemAdditions.csv')
    Utils.writeOrderItemModificationToCsv(orderItemSubtractions, 'data/OrderItemSubtractions.csv')



