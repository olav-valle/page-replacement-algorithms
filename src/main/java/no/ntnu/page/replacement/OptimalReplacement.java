package no.ntnu.page.replacement;

import java.util.List;

/**
 * Optimal Replacement algorithm
 * Fill in your code in this class!
 */
public class OptimalReplacement extends ReplacementAlgorithm {

    // TODO - add some state variables here, if you need any

    @Override
    protected void reset() {
        // TODO - do preparation/initialization here, if needed
    }

    @Override
    public int process(String referenceString) {
        List<Integer> pageReferences = Tools.stringToArray(referenceString);

        //if page reference list is empty,
        // or tool method somehow returned null,
        // we do zero replacements.
        if (pageReferences == null || pageReferences.size() == 0) return 0;

        int replacements = 0; // How many page replacements made

        //traverse pageReferences
        for (int i = 0; i < pageReferences.size(); i++) {
            // if page is not in frame
            if (!isLoaded(pageReferences.get(i))) {
                //miss!

                //find victim frame using prediction method
                int victimIndex = findFarthestFuturePage(pageReferences, i + 1);
                boolean replaced;
                // put page in frame
                if (victimIndex == -1) {
                    // we can use any frame, and chose frame index 0 as default.
                    replaced = pageIn(0, pageReferences.get(i));
                } else {
                    replaced = pageIn(victimIndex, pageReferences.get(i));
                    // use frame returned by prediction method
                }

                if (replaced) replacements++;

                System.out.println(getFrameStatus());
            }
        }

        return replacements;
    }

    /**
     * Checks if any of the page numbers currently in a frame are present among the
     * remaining values of the input array (the "future").
     * <p>
     * Returns the index of the "victim frame", i.e. the index in frames[] of the
     * page number that is used "farthest ahead in the future", or is never used again.
     * <p>
     * Returns -1 if if none of the pages currently in frames are used again, i.e.
     * all frames are valid "victim frame" candidates.
     * <p>
     * Example:
     * current pages in frames: [2, 4, 5]
     * remaining page numbers in pages: [3, 4, 1, 0, 2, 0, 4, 5, 1, 4]
     * Of the page numbers in currently in frames (2, 4, 5), 5 is first used
     * again after both 4 and 2 are used again. Therefore, the return value is the
     * index of 5 in the frames[] array, i.e. 2.
     * <p>
     * In another example, if the current pages in frames were [6, 9, 7],
     * neither of those three numbers come up again in the input array.
     * Therefore, the return would be -1, indicating that either
     */
    private int findFarthestFuturePage(List<Integer> pageReferences, int nextPageRefIndex) {
        int victimIdx = -1; //
        int farthestPageIndex = nextPageRefIndex; // start looking after current page ref
        int maxPageIndex = pageReferences.size() - 1; // So we know when to stop looking

        // check for empty frames.
        int emptyFrame = findEmptyFrame(); // -1 if all frames are full
        if (emptyFrame != -1) {
            return emptyFrame;
        }

        //traverse frames
        for (int f = 0; f < frames.length; f++) {

            //traverse pageRefs from current idx
            for (int p = nextPageRefIndex; p < pageReferences.size(); p++) {

                //if page is in current frame
                if (frames[f] == pageReferences.get(p)) {

                    // we start next frame iteration at current location, since any page ref
                    //  before this one is a worse choice
                    if (p > farthestPageIndex) {
                        farthestPageIndex = p;
                        victimIdx = f;
                    }

                    break; //break pageRefs traversal at first match

                } else if (p == maxPageIndex) {
                    //end of pageRefs list
                    // page in frames[f] is never used again,
                    // and is the best candidate.
                    return f;
                }
            }
        }
        return victimIdx;
    }

    /**
     * Finds index of the first empty frame.
     *
     * @return Index of first empty frame, or -1 if all frames are loaded.
     */
    private int findEmptyFrame() {
        for (int f = 0; f < frames.length; f++) {
            if (frames[f] == -1) return f;
        }
        return -1;
    }
}