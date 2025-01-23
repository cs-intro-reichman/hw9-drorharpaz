/**
 * Represents a managed memory space. The memory space manages a list of allocated 
 * memory blocks, and a list free memory blocks. The methods "malloc" and "free" are 
 * used, respectively, for creating new blocks and recycling existing blocks.
 */
public class MemorySpace {
	
	// A list of the memory blocks that are presently allocated
	private LinkedList allocatedList;

	// A list of memory blocks that are presently free
	private LinkedList freeList;

	/**
	 * Constructs a new managed memory space of a given maximal size.
	 * 
	 * @param maxSize
	 *            the size of the memory space to be managed
	 */
	public MemorySpace(int maxSize) {
		// initiallizes an empty list of allocated blocks.
		allocatedList = new LinkedList();
	    // Initializes a free list containing a single block which represents
	    // the entire memory. The base address of this single initial block is
	    // zero, and its length is the given memory size.
		freeList = new LinkedList();
		freeList.addLast(new MemoryBlock(0, maxSize));
	}

	/**
	 * Allocates a memory block of a requested length (in words). Returns the
	 * base address of the allocated block, or -1 if unable to allocate.
	 * 
	 * This implementation scans the freeList, looking for the first free memory block 
	 * whose length equals at least the given length. If such a block is found, the method 
	 * performs the following operations:
	 * 
	 * (1) A new memory block is constructed. The base address of the new block is set to
	 * the base address of the found free block. The length of the new block is set to the value 
	 * of the method's length parameter.
	 * 
	 * (2) The new memory block is appended to the end of the allocatedList.
	 * 
	 * (3) The base address and the length of the found free block are updated, to reflect the allocation.
	 * For example, suppose that the requested block length is 17, and suppose that the base
	 * address and length of the the found free block are 250 and 20, respectively.
	 * In such a case, the base address and length of of the allocated block
	 * are set to 250 and 17, respectively, and the base address and length
	 * of the found free block are set to 267 and 3, respectively.
	 * 
	 * (4) The new memory block is returned.
	 * 
	 * If the length of the found block is exactly the same as the requested length, 
	 * then the found block is removed from the freeList and appended to the allocatedList.
	 * 
	 * @param length
	 *        the length (in words) of the memory block that has to be allocated
	 * @return the base address of the allocated block, or -1 if unable to allocate
	 */
	// public int malloc(int length) {		
	// 	if (length <= 0) {
	// 		return -1;
	// 	}
	// 	for (int i = 0;i < freeList.getSize();i ++){
	// 		if (freeList.getBlock(i).length >= length) {
	// 			MemoryBlock newMemoryBlock = new MemoryBlock(freeList.getBlock(i).baseAddress, length);
	// 			freeList.getBlock(i).baseAddress = newMemoryBlock.baseAddress + length;
	// 			freeList.getBlock(i).length = freeList.getBlock(i).length - length;
	// 			allocatedList.addLast(newMemoryBlock);
	// 			if (freeList.getBlock(i).length == 0) 	{freeList.remove(i);}
	// 			return newMemoryBlock.baseAddress;
	// 		}
	// 	}
	// 	return -1;
	// }

	public int malloc(int length) {
		if (length <= 0) {
			return -1; // אי אפשר להקצות בלוק בגודל 0 או שלילי
		}
		for (int i = 0; i < freeList.getSize(); i++) {
			MemoryBlock freeBlock = freeList.getBlock(i);
			if (freeBlock.length >= length) {
				// יצירת בלוק חדש להקצאה
				MemoryBlock allocatedBlock = new MemoryBlock(freeBlock.baseAddress, length);
	
				// עדכון הבלוק הפנוי
				freeBlock.baseAddress += length;
				freeBlock.length -= length;
	
				// אם האורך של הבלוק הפנוי הוא 0, הסר אותו
				if (freeBlock.length == 0) {
					freeList.remove(i);
				}
	
				// הוסף את הבלוק החדש לרשימת המוקצים
				allocatedList.addLast(allocatedBlock);
				return allocatedBlock.baseAddress; // החזר את הכתובת
			}
		}
		return -1; // לא נמצא מקום פנוי
	}

	/**
	 * Frees the memory block whose base address equals the given address.
	 * This implementation deletes the block whose base address equals the given 
	 * address from the allocatedList, and adds it at the end of the free list. 
	 * 
	 * @param baseAddress
	 *            the starting address of the block to freeList
	 */
	// public void free(int address) {
	// 	for (int i = 0; i < allocatedList.getSize();i ++){
	// 		if (allocatedList.getBlock(i).baseAddress == address) {
	// 			freeList.addLast(allocatedList.getBlock(i));
	// 			allocatedList.remove(i);
	// 		}
	// 	}
	// }

	public void free(int address) {
		for (int i = 0; i < allocatedList.getSize(); i++) {
			MemoryBlock allocatedBlock = allocatedList.getBlock(i);
			if (allocatedBlock.baseAddress == address) {
				// הוסף את הבלוק לרשימה הפנויה
				freeList.addLast(allocatedBlock);
				allocatedList.remove(i); // הסר את הבלוק מהרשימה המוקצה
				return;
			}
		}
		throw new IllegalArgumentException("Address not found in allocated list");
	}
	
	/**
	 * A textual representation of the free list and the allocated list of this memory space, 
	 * for debugging purposes.
	 */
	public String toString() {
		return freeList.toString() + "\n" + allocatedList.toString() + "\n";		
	}
	
	/**
	 * Performs defragmantation of this memory space.
	 * Normally, called by malloc, when it fails to find a memory block of the requested size.
	 * In this implementation Malloc does not call defrag.
	 */
	// public void defrag() {
	// 	for (int i = 0; i < this.freeList.getSize();i ++){
	// 		for (int j = 0;j < this.freeList.getSize(); j ++){
	// 			if ((freeList.getBlock(i).baseAddress + freeList.getBlock(i).length) == freeList.getBlock(j).baseAddress) {
	// 				freeList.getBlock(i).length = freeList.getBlock(i).length + freeList.getBlock(j).length;
	// 				freeList.remove(j);
	// 				i = 0;
	// 				j = 0;
	// 			}else if (freeList.getBlock(j).baseAddress + freeList.getBlock(j).length == freeList.getBlock(i).baseAddress) {
	// 				freeList.getBlock(j).length = freeList.getBlock(j).length + freeList.getBlock(i).length;
	// 				freeList.remove(i);
	// 				i = 0;
	// 				j = 0;
	// 			}
				
	// 		}
	// 	}
	// }

	public void defrag() {
		for (int i = 0; i < freeList.getSize(); i++) {
			MemoryBlock current = freeList.getBlock(i);
	
			for (int j = 0; j < freeList.getSize(); j++) {
				if (i == j) continue; // דלג על השוואה עצמית
	
				MemoryBlock other = freeList.getBlock(j);
	
				// בדוק אם הבלוקים ברצף
				if (current.getEndAddress() == other.baseAddress) {
					// איחוד current ו-other
					current.length += other.length;
					freeList.remove(j);
					if (j < i) i--; // עדכון האינדקס אם בלוק קודם הוסר
					j--; // המשך לבדוק בלוקים נוספים עם current
				} else if (other.getEndAddress() == current.baseAddress) {
					// איחוד other ו-current
					other.length += current.length;
					other.baseAddress = current.baseAddress;
					freeList.remove(i);
					i--; // עדכון האינדקס לאחר הסרה
					break; // הפסק את הלולאה הפנימית כי current הוסר
				}
			}
		}
	}

}
