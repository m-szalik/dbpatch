-- block - my create function block
CREATE FUNCTION FUNC1 (x1 decimal, y1 decimal)
RETURNS decimal
DETERMINISTIC
BEGIN
  DECLARE dist decimal;
  SET dist = SQRT(x1 - y1);
  RETURN dist;
END
-- single line comment indicate end of block
