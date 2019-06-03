library(plot3D)

y <- c(2, 3, 4, 5, 6, 7, 8, 9, 10, 11)
x <- c(2, 3, 4, 5, 6, 7, 8)

name <- "Rastrigin"

z <- as.matrix(read.csv("values.csv", sep=",", header=FALSE))
rownames(z) <- x
colnames(z) <- y
persp3D(x,y,z, xlab="Recursion Depth", ylab="Population Size", zlab="Function Value", theta=110, phi=35, clab=name)

z <- as.matrix(read.csv("times.csv", sep=",", header=FALSE))
rownames(z) <- x
colnames(z) <- y
persp3D(x,y,z, xlab="Recursion Depth", ylab="Population Size", zlab="Time [ms]", theta=-35, phi=35, clab=name)

z <- as.matrix(read.csv("evaluations.csv", sep=",", header=FALSE))
rownames(z) <- x
colnames(z) <- y
persp3D(x,y,z, xlab="Recursion Depth", ylab="Population Size", zlab="Number of Evaluations", theta=-35, phi=35, clab=name)

name <- "Griewank"

z <- as.matrix(read.csv("values.csv", sep=",", header=FALSE))
rownames(z) <- x
colnames(z) <- y
persp3D(x,y,z, xlab="Recursion Depth", ylab="Population Size", zlab="Function Value", theta=110, phi=35, clab=name)

z <- as.matrix(read.csv("times.csv", sep=",", header=FALSE))
rownames(z) <- x
colnames(z) <- y
persp3D(x,y,z, xlab="Recursion Depth", ylab="Population Size", zlab="Time [ms]", theta=-35, phi=35, clab=name)

z <- as.matrix(read.csv("evaluations.csv", sep=",", header=FALSE))
rownames(z) <- x
colnames(z) <- y
persp3D(x,y,z, xlab="Recursion Depth", ylab="Population Size", zlab="Number of Evaluations", theta=-35, phi=35, clab=name)

