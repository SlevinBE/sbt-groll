/*
 * Copyright 2013 Heiko Seeberger
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package name.heikoseeberger.sbtgroll

import java.io.File
import org.eclipse.jgit.api.{ Git => JGit, ResetCommand }
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.revwalk.{ RevCommit, RevWalk }
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import org.eclipse.jgit.treewalk.{ AbstractTreeIterator, CanonicalTreeParser }
import scala.collection.JavaConversions.iterableAsScalaIterable

object Git {
  def apply(workTree: File): Git =
    new Git((new FileRepositoryBuilder).setWorkTree(workTree).build())
}

class Git(repository: Repository) {

  private val jgit = new JGit(repository)

  def checkout(ref: String, branch: String = "groll"): Unit = {
    jgit.checkout.setName("master").call()
    jgit.branchDelete.setBranchNames(branch).setForce(true).call()
    jgit.checkout.setStartPoint(ref).setName(branch).setCreateBranch(true).call()
  }

  def clean(): Unit =
    jgit.clean.setCleanDirectories(true).setIgnore(true).call()

  def current(): (String, String) =
    (jgit.log.setMaxCount(1).call().toList map idAndMessage).head

  def diff(newRef: String, oldRef: String): Seq[String] =
    jgit.diff.setNewTree(tree(newRef)).setOldTree(tree(oldRef)).call.toList map (_.getNewPath)

  def history(ref: String = "master"): Seq[(String, String)] =
    jgit.log.add(repository.resolve(ref)).call().toList map idAndMessage

  def resetHard(): Unit =
    jgit.reset.setMode(ResetCommand.ResetType.HARD).call()

  private def idAndMessage(commit: RevCommit) =
    commit.shortId -> commit.getShortMessage

  private def tree(ref: String): AbstractTreeIterator = {
    val tree = {
      val walk = new RevWalk(repository)
      val commit = walk.parseCommit(repository.resolve(ref))
      walk.parseTree(commit.getTree.getId)
    }
    val parser = new CanonicalTreeParser
    val reader = repository.newObjectReader
    try {
      parser.reset(reader, tree.getId)
      parser
    } finally reader.release()
  }
}
