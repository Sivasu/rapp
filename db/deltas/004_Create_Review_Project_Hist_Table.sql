CREATE TABLE tbl_Reviewer_Project_Hist (
ID INT AUTO_INCREMENT PRIMARY KEY,
ReviewerID INT,
ProjectName VARCHAR(20),
StartDate DATE,
EndDate DATE,
FOREIGN KEY (ReviewerID) REFERENCES tbl_Reviewer(ID)
);

--//@UNDO

DROP TABLE tbl_Reviewer_Project_Hist;